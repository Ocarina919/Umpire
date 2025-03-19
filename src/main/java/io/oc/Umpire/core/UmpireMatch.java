package io.oc.Umpire.core;

import io.oc.Umpire.*;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Instrument;
import org.bukkit.Note;
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

    public UmpireMatch(UmpireMap map){
        this.map = map;
        map.startWorld();
        map.loadMapFromXML(this);
        map.world.setGameRule(GameRule.DO_INSOMNIA, false);

        teams.add(obsTeam);
        this.state = State.PREGAME;
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
