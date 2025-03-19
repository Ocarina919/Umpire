package io.oc.Umpire.core;

import static org.bukkit.Bukkit.getLogger;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class UmpirePlayer {
    public UmpirePlayer(Player bukkitPlayer){
        this.bukkitPlayer = bukkitPlayer;
    }

    public UmpireTeam team;
    public UmpireMatch match;
    public Player bukkitPlayer;
    
    public ItemStack[] savedInventory = new ItemStack[37];
    public ItemStack[] savedArmor = new ItemStack[4];

    
    public void setColor(ChatColor color){
        bukkitPlayer.setDisplayName(color + bukkitPlayer.getName() + ChatColor.WHITE);
        bukkitPlayer.setPlayerListName(color + bukkitPlayer.getName() + ChatColor.WHITE);
    }
    
    

    public void clearColor(){
        setColor(ChatColor.WHITE);
    }
    
    public void wipePlayer() {
        getLogger().info("Wiping player " + bukkitPlayer.getName());
    	bukkitPlayer.setLevel(0);
    	bukkitPlayer.setExp(0);
    	bukkitPlayer.setHealth(bukkitPlayer.getAttribute(Attribute.MAX_HEALTH).getValue());
    	bukkitPlayer.setFoodLevel(20);
    	bukkitPlayer.setSaturation(5); //Default on world generation is 5
    	bukkitPlayer.getInventory().clear();
    	
    	for(PotionEffect pe : bukkitPlayer.getActivePotionEffects()) {
    		bukkitPlayer.removePotionEffect(pe.getType());
    	}
    }
    
    public void loadArmor() {
    	bukkitPlayer.getInventory().setItem(36, savedArmor[0]);
    	bukkitPlayer.getInventory().setItem(37, savedArmor[1]);
    	bukkitPlayer.getInventory().setItem(38, savedArmor[2]);
    	bukkitPlayer.getInventory().setItem(39, savedArmor[3]);

    }
    
    public void saveArmor() {
    	savedArmor[0] = bukkitPlayer.getInventory().getItem(36);
    	savedArmor[1] = bukkitPlayer.getInventory().getItem(37);
    	savedArmor[2] = bukkitPlayer.getInventory().getItem(38);
    	savedArmor[3] = bukkitPlayer.getInventory().getItem(39);
    }
    
    public void loadInventory() {
    	bukkitPlayer.getInventory().setItem(40, savedInventory[36]);
    	
    	for(int x = 0; x < 36; x++) {
    		bukkitPlayer.getInventory().setItem(x, savedInventory[x]);
    	}
    }
    
    public void saveInventory() {
    	savedInventory[36] = bukkitPlayer.getInventory().getItem(40);
    	
    	for(int x = 0; x < 36; x++) {
    		savedInventory[x] = bukkitPlayer.getInventory().getItem(x);
    	}
    }
    
    public void clearInventory() {
    	for(int x = 0; x < savedInventory.length; x++) {
    		savedInventory[x] = null;
    	}
    }
    
    public void clearArmor() {
    	for(int x = 0; x < savedArmor.length; x++) {
    		savedArmor[x] = null;
    	}
    }
    
}
