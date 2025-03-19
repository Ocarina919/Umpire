package io.oc.Umpire;

import static io.oc.Umpire.Umpire.getInstance;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;
import static org.bukkit.Bukkit.getServer;

import java.io.File;
import java.util.*;

import io.oc.Umpire.core.*;
import io.oc.Umpire.practice.Practice;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.oc.Umpire.utils.MapUtils;

interface RunUmpireCommand {
    boolean run(String[] args, Player p, UmpirePlayer up);
}

class UmpireCommand{
    Set<Integer> argCount;
    RunUmpireCommand runner;
    boolean commandBlockCommand;
    UmpireCommand(Set<Integer> argCount, RunUmpireCommand runner, boolean commandBlockCommand){
        this.argCount = argCount;
        this.runner = runner;
        this.commandBlockCommand = commandBlockCommand;
    }
    boolean run(String[] args, CommandSender cs){
        Player p;

        if(cs instanceof Player){
            p = (Player)cs;
        }else if(cs instanceof BlockCommandSender && commandBlockCommand){
            p = getPlayer(args[args.length-1]);
            if(p == null){
                return false;
            }
            args = Arrays.copyOfRange(args, 0, args.length-1);
        }else{
            return false;
        }

        if(!argCount.contains(args.length)){
            return false;
        }

        UmpirePlayer up = Umpire.getPlayer(p);
        getLogger().info("Running UmpireCommand as " + p.getName() + " with: " + Arrays.toString(args));
        return runner.run(args, p, up);
    }
}
public class Commands extends HashMap<String, UmpireCommand> {
    Commands(){

        //Commands need to be added here, below, and in plugin.yml

        getLogger().info("Adding Commands");
        this.put("loadmap", new UmpireCommand(Set.of(1,2,3,4,5,6,7,8,9,10),this::loadmap,false));
        this.put("lobby", new UmpireCommand(Set.of(0),this::lobby, false));
        this.put("join", new UmpireCommand(Set.of(0,1,2),this::join, true));
        this.put("leave", new UmpireCommand(Set.of(0,1),this::leave, true));
        this.put("ready", new UmpireCommand(Set.of(0),this::ready, false));
        this.put("unready", new UmpireCommand(Set.of(0),this::unready, false));
        this.put("unload", new UmpireCommand(Set.of(0),this::unload, false));
        this.put("reloadmap", new UmpireCommand(Set.of(0),this::reloadmap, false));
        this.put("joinmatch", new UmpireCommand(Set.of(1),this::joinmatch, false));
        this.put("viewinventory", new UmpireCommand(Set.of(1),this::viewinventory, false));
        this.put("maps", new UmpireCommand(Set.of(0,1,2,3,4,5,6,7,8,9,10),this::maps,false));
        this.put("practice", new UmpireCommand(Set.of(0),this::practice,false));
    }
    private boolean viewinventory(String[] args, Player p, UmpirePlayer up) {
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null){
            return false;
        }
        if(!up.team.isObs){
            return false;
        }

        p.openInventory(target.getInventory());
        return true;
    }
    private boolean loadmap(String[] args, Player p, UmpirePlayer up){
        UmpireMatch oldMatch = up.match;

        String mapName = String.join("", args);
        mapName = mapName.toLowerCase();

        String bestMap = MapUtils.findMap(mapName);
        if (bestMap == null){return false;}

        MapDescriptor mapDescriptor = new MapDescriptor(bestMap);
        p.sendMessage(ChatColor.GRAY + "Loading Map: " + ChatColor.WHITE + ChatColor.BOLD + mapDescriptor.name + ChatColor.WHITE);
        UmpireMap umpireMap = new UmpireMap(mapDescriptor);

        UmpireMatch match = new UmpireMatch(umpireMap);

        match.addPlayer(up);
        p.teleport(match.obsTeam.spawnPoint);

        getInstance().addMatch(match);

        if(oldMatch != null){
            oldMatch.checkUnused();
        }

        return true;
    }
    private boolean lobby(String[] args, Player p, UmpirePlayer up){
        if(up.team != null) {//Should never be null anyways since obs is a team, but oh well...
            up.team.removePlayer(up);
        }

        UmpireMatch oldmatch = up.match;

        up.team = null;
        up.match = null;
        up.bukkitPlayer.teleport(getServer().getWorlds().get(0).getSpawnLocation());

        if(oldmatch != null) {
            oldmatch.checkUnused();
        }

        return true;
    }
    private boolean join(String[] args, Player p, UmpirePlayer up){
        UmpireTeam newTeam;

        if(args.length == 1) {
            newTeam = up.match.getTeam(args[0]);
        }else{
            Random rand = new Random();
            int attempts = 0;
            do{
                newTeam = up.match.teams.get(rand.nextInt(up.match.teams.size()));
                attempts++;
                if(attempts > 20){
                    p.sendMessage("Could not find other team to join");
                    return true;
                }
            }while(newTeam.isObs || newTeam.teamname.equalsIgnoreCase(up.team.teamname));

        }
        if (newTeam == null){return false;}

        if(up.team != null){
            up.team.removePlayer(up);
        }
        up.team = newTeam;
        up.team.addPlayer(up);
        p.teleport(newTeam.spawnPoint);

        up.match.broadcast(newTeam.color + p.getName() + ChatColor.WHITE + " has joined team " + newTeam.color +  newTeam.teamname + ChatColor.WHITE);
        return true;

    }
    private boolean leave(String[] args, Player p, UmpirePlayer up){
        if(up.team != null) {
            up.team.removePlayer(up);
        }
        up.team = up.match.obsTeam;
        up.team.addPlayer(up);
        getLogger().info(up.match.map.world.getName());
        getLogger().info(up.team.spawnPoint.toString());
        p.teleport(up.team.spawnPoint);

        up.match.broadcast(up.match.obsTeam.color + p.getName() + ChatColor.WHITE + " has joined team " + up.match.obsTeam.color + up.match.obsTeam.teamname + ChatColor.WHITE);

        return true;
    }
    private boolean ready(String[] args, Player p, UmpirePlayer up){
        if(up.match == null){return false;}
        if (up.team != null){
            up.team.readyState = true;

            up.match.broadcast(up.team.color + up.team.teamname + ChatColor.WHITE + " is " + ChatColor.GREEN + "ready" + ChatColor.WHITE);

            up.match.checkIfStart();
            return true;
        }
        return false;
    }
    private boolean unready(String[] args, Player p, UmpirePlayer up){
        if(up.match == null){return false;}
        if (up.team != null){
            up.team.readyState = false;
            up.match.cancelStart();

            up.match.broadcast(up.team.color + up.team.teamname + ChatColor.WHITE +  " is " + ChatColor.RED + "not ready" + ChatColor.WHITE);

            return true;
        }
        return false;
    }
    private boolean unload(String[] args, Player p, UmpirePlayer up){
        if(up.team != null) {//Should never be null anyways since obs is a team, but oh well...
            up.team.removePlayer(up);
        }
        up.team = null;
        p.teleport(getServer().getWorlds().get(0).getSpawnLocation());

        up.match.deload();

        up.match = null;
        return true;
    }
    private boolean reloadmap(String[] args, Player p, UmpirePlayer up){
        UmpireTeam oldTeam = up.team;
        up.team = null;
        UmpireMatch oldMatch = up.match;

        p.sendMessage("Reloading Map");
        MapDescriptor mapDescriptor = new MapDescriptor(oldMatch.map.mapDescriptor.mapName);
        UmpireMap umpireMap = new UmpireMap(mapDescriptor);

        UmpireMatch match = new UmpireMatch(umpireMap);

        for(UmpirePlayer otherPlayer: oldMatch.getPlayers()){
            match.addPlayer(otherPlayer);
            otherPlayer.bukkitPlayer.teleport(match.obsTeam.spawnPoint);
        }

        getInstance().addMatch(match);

        if(oldTeam != null) {//Should never be null anyways since obs is a team, but oh well...
            oldTeam.removePlayer(up);
        }

        oldMatch.deload();
        //TODO oldmatch never gets set to null?
        return true;
    }

    private boolean joinmatch(String[] args, Player p, UmpirePlayer up){
        UmpirePlayer target = Umpire.getPlayer(Bukkit.getPlayer(args[0]));
        up.wipePlayer();
        p.setGameMode(GameMode.CREATIVE);
        target.match.addPlayer(up);
        p.teleport(target.match.obsTeam.spawnPoint);
        return true;
    }
    
    private boolean maps(String[] args, Player p, UmpirePlayer up) {
        int page = 1;
        String author = "";
        String name = "";
        Set<String> tags = new HashSet<>();
        String argType = "-p";
        List<String> argTypes = List.of(new String[]{"-p", "-a", "-n", "-t", "-v"});
        boolean verbose = false;
        for (String arg : args) {
            getLogger().info("arg: " + arg);
            if(argTypes.contains(arg)) {
                argType = arg;
                if(arg.equals("-v")){
                    verbose = true;
                }
            }
            else {
                switch (argType) {
                    case ("-p"):
                        try {
                            page = Integer.parseInt(arg);
                        } catch (NumberFormatException e) {
                            getLogger().info("Number format exception trying to parse " + arg);
                            return false;
                        }
                        argType = "";
                        break;
                    case ("-a"):
                        author = arg;
                        argType = "";
                        break;
                    case ("-n"):
                        name = arg;
                        argType = "";
                        break;
                    case ("-t"):
                        tags.add(arg);
                        break;
                    default:
                        getLogger().info("Default case");
                        return false;
                }
            }
        }

    	String[] mapFiles = (new File("maps")).list(); //Creates new file 'maps' and then lists everything in the directory 'maps'

        List<MapDescriptor> filtered = new ArrayList<>();
        for (String mapFile : mapFiles){
            MapDescriptor md = new MapDescriptor(mapFile);
            boolean authorMatch = false;
            for(String mapAuthor : md.authors){
                if(mapAuthor.toLowerCase().contains(author.toLowerCase())){
                    authorMatch = true;
                    break;
                }
            }
            if(!authorMatch) continue;
            if(!md.name.toLowerCase().contains(name.toLowerCase())) continue;

            getLogger().info("md.tags: " + md.tags + ", tags: " + tags);
            if(!md.tags.containsAll(tags)) continue;
            filtered.add(md);
        }

    	int upperbound;
    	int lowerbound;

        int maxPages = filtered.size()/10;
        if(filtered.size()%10 > 0)
            maxPages++;

        if(page > maxPages || page < 1) {
            p.sendMessage("This page does not exist, there are only " + maxPages + " pages of maps");
        }else {
            upperbound = Math.min(page * 10, filtered.size());
            lowerbound = (page - 1) * 10;

            p.sendMessage(ChatColor.GRAY + "Showing Page " + page + " of " + maxPages + ChatColor.WHITE);
            for(int i = lowerbound; i < upperbound; i++) {
                MapDescriptor md = filtered.get(i);
                if(verbose){
                    p.sendMessage(md.name + " v" + md.version + " by " + String.join(" and ", md.authors));//include other authors
                }else{
                    p.sendMessage(md.name);
                }
            }
            return true;
        }

    	
    	return false;
    }
    
    private boolean practice(String[] args, Player p, UmpirePlayer up) {
        //No need to redo items when they're already there
        if(Practice.practiceInventory == null) {
            Practice.init();
        }

    	p.openInventory(Practice.practiceInventory);
    	return true;
    }
    
}
