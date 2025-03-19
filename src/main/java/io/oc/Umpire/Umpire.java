package io.oc.Umpire;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import io.oc.Umpire.core.MapCleaner;
import io.oc.Umpire.core.UmpireMatch;
import io.oc.Umpire.core.UmpirePlayer;
import io.oc.Umpire.listeners.LiquidListener;
import io.oc.Umpire.listeners.MapPracticeListener;
import io.oc.Umpire.listeners.PlayerConnectionListener;
import io.oc.Umpire.listeners.PlayerResourceListener;
import io.oc.Umpire.listeners.RespawnListener;
import io.oc.Umpire.listeners.SwimmingListener;
import io.oc.Umpire.listeners.VictoryConditionListener;
import io.oc.Umpire.listeners.ViewInventoryListener;
import io.oc.Umpire.listeners.VoidListener;
import io.oc.Umpire.utils.MapUtils;


public class Umpire extends JavaPlugin{
    private static Umpire instance = null;
    public Set<UmpireMatch> matches = new HashSet<>();
    private static Set<UmpirePlayer> players = new HashSet<>();
    public static Scoreboard scoreboard;

    @Override
    public void onEnable() {
        instance = this;
        scoreboard = getServer().getScoreboardManager().getMainScoreboard();
        scoreboard.getTeams().forEach(Team::unregister);

        getLogger().info("Adding Listeners");
        getServer().getPluginManager().registerEvents(new SwimmingListener(), this);
        getServer().getPluginManager().registerEvents(new RespawnListener(), this);
        getServer().getPluginManager().registerEvents(new LiquidListener(), this);
        getServer().getPluginManager().registerEvents(new VoidListener(), this);
        getServer().getPluginManager().registerEvents(new VictoryConditionListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(), this);
        getServer().getPluginManager().registerEvents(new ViewInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerResourceListener(), this);
        getServer().getPluginManager().registerEvents(new MapPracticeListener(), this);

        Commands commands = new Commands();
        CommandHandler handler = new CommandHandler();

        for (String command : commands.keySet()){
            getLogger().info("Adding handler for: " + command);
            PluginCommand pluginCommand = this.getCommand(command);
            if(pluginCommand != null){
                pluginCommand.setExecutor(handler);
            }
            else{
                getLogger().info("Command " + command + " has not been added to plugin.yml file, skipping it");
            }
        }

        int deleted = MapUtils.cleanOrphanedFolders();
        getLogger().info("Cleaned up " + deleted + " orphaned world folders");

        MapCleaner cleaner = new MapCleaner(this);
        cleaner.runTaskTimerAsynchronously(getInstance(), 0L, 30L*20L);

    }

    public static Umpire getInstance(){
        return instance;
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }

    public void addMatch(UmpireMatch match){
        matches.add(match);
    }

    public static UmpirePlayer getPlayer(Player player){
        for (UmpirePlayer umpirePlayer : players){
            if (umpirePlayer.bukkitPlayer.getUniqueId().equals(player.getUniqueId())){
                return umpirePlayer;
            }
        }
        return null;
    }

    public static UmpirePlayer addPlayer(Player player){
        UmpirePlayer newPlayer = new UmpirePlayer(player);
        players.add(newPlayer);
        return newPlayer;
    }

    public static UmpireMatch getMatch(String worldName){
        //Arrays.stream(worldFolders).map(file -> getMatch(file.getName()).map.worldName != null).reduce(true, (prev, next) -> (prev && next));
        for (UmpireMatch um : getInstance().matches){
            if (um.map.worldName.equalsIgnoreCase(worldName)){
                return um;
            }
        }
        return null;
    }

}
