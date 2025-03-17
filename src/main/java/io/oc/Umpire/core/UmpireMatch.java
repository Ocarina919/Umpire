package io.oc.Umpire.core;

import io.oc.Umpire.*;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import static org.bukkit.Bukkit.getLogger;

public class UmpireMatch {

    public UmpireTeam obsTeam = new UmpireTeam(ChatColor.GRAY, "Observers", true);
    public ArrayList<UmpireTeam> teams = new ArrayList<>();
    public UmpireMap map;
    public State state;
    CountdownTimer startTimer;
    public boolean retro;

    public UmpireMatch(UmpireMap map, boolean retro){
        this.map = map;

        if(retro) {
            YamlConfiguration config = Bukkit.spigot().getConfig();
            ConfigurationSection worldSettings = config.getConfigurationSection("world-settings");
            ConfigurationSection newSection = worldSettings.createSection(map.worldName);
            ConfigurationSection hungerSection = newSection.createSection("hunger");

            hungerSection.set("jump-walk-exhaustion", 0.2f);
            hungerSection.set("jump-sprint-exhaustion", 0.8f);
            hungerSection.set("combat-exhaustion", 0.3f);
            //hungerSection.set("regen-exhaustion", 4.0f);Regen by default has scaling exhaustion. Not sure what this even does.
            hungerSection.set("swim-multiplier", 0.015f);
            //hungerSection.set("sprint-multiplier", 0.1f); This is unchanged
            hungerSection.set("other-multiplier", 0.01f);
        }

        map.startWorld();

        map.loadMapFromXML(this);

        teams.add(obsTeam);
        this.state = State.PREGAME;
        this.retro = retro;
    }

    public void broadcast(String message){
        getPlayers().forEach(p -> p.bukkitPlayer.sendMessage(message));
        //TODO test
    }

    public void broadcastTitle(String title, String subtitle){
        Set<UmpirePlayer> players = getPlayers();
        for(UmpirePlayer player : players){
            player.bukkitPlayer.sendTitle(title, subtitle, 5, 60, 20);
            getLogger().info("Broadcasting to " + player.bukkitPlayer.getName());
        }
        //getPlayers().forEach(p -> p.bukkitPlayer.sendTitle(title, subtitle, 5, 60, 20));
        //TODO test
    }

    public void broadcastNote(Note note){
        getPlayers().forEach(p -> p.bukkitPlayer.playNote(p.bukkitPlayer.getLocation(), Instrument.PLING, note));
    }

    public void addPlayer(UmpirePlayer player) {
        if(retro){
            player.bukkitPlayer.setSaturatedRegenRate(80);
        }
        obsTeam.addPlayer(player);
        player.wipePlayer();
        player.match = this;
    }

    public Set<UmpirePlayer> getPlayers(){
        return teams.stream().flatMap(team -> (
            team.players.stream()
        )).collect(Collectors.toSet());
    }

    public void checkIfStart() {
        for (UmpireTeam team : teams){
            //Don't start the game if there is a legitamate (not obs or empty) team that is not ready
            if (!team.isObs && !team.readyState && !team.players.isEmpty()){
                return;
            }
        }
        if(state == State.PREGAME) {
            this.startCountdown();
        }
    }

    private void startCountdown(){
        broadcast("Match will begin in...");
        getLogger().info("Starting countdown");
        startTimer = new CountdownTimer(this);
        startTimer.runTaskTimer(Umpire.getInstance(), 0L, 20L);
    }

    public void cancelStart(){
        if(startTimer != null && !startTimer.isCancelled()) {
            startTimer.cancel();
        }
    }

    public void startMatch(){
        this.state = State.PLAYING;
        //full heal, full food, clear inventory, gamemode
        for(UmpirePlayer up: getPlayers()){
            Player p = up.bukkitPlayer;
            up.wipePlayer();
            p.setGameMode(GameMode.SURVIVAL);
        }

        map.startMechanisms();
        getLogger().info("Starting match!");
        broadcastTitle("Go!", "");
        broadcastNote(new Note(1, Note.Tone.F, true));
        broadcast("Go!");
    }

    public UmpireTeam getTeam(String teamName){
        for (UmpireTeam team : teams){
            if (team.teamname.equalsIgnoreCase(teamName)){
                return team;
            }
        }
        return null;
    }

    public void deload() {
        map.deload();
    }

    public void checkUnused() {
        for (UmpireTeam team : teams){
            if (!team.players.isEmpty()){
                return;
            }
        }
        map.deload();
    }

    public void checkVictory() {
        boolean completed;
        for (UmpireTeam team : teams){
            completed = true;
            for (VictoryCondition vc : map.victoryConditions){
                if(team.isObs){
                    completed = false;
                }
                if (vc.teamName.equalsIgnoreCase(team.teamname)){
                    completed &= vc.check();
                }
            }
            if (completed){
                endMatch(team);
            }
        }
    }

    private void endMatch(UmpireTeam team) {
        getLogger().info("Ending match with winner: " + team.teamname);
        broadcast("Game Over! " + team.color + team.teamname + ChatColor.WHITE + " wins the match!");
        for(UmpirePlayer up : getPlayers()){
            up.bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        }
    }
}
