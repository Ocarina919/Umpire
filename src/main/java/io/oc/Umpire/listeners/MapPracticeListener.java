package io.oc.Umpire.listeners;

import io.oc.Umpire.practice.Practice;

import io.oc.Umpire.practice.PracticeCommand;
import io.oc.Umpire.practice.PracticeRunnable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import io.oc.Umpire.Umpire;

public class MapPracticeListener implements Listener{

	@EventHandler
	public void onInventoryClick(InventoryDragEvent e) {
		if(e.getInventory().equals(Practice.practiceInventory)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(e.getInventory().equals(Practice.practiceInventory)) {
			e.setCancelled(true);
			if(e.getClick().equals(ClickType.RIGHT) || e.getClick().equals(ClickType.LEFT)) {
				Player player = (Player) e.getWhoClicked();
				int slot = e.getSlot();

				if(0 <= slot && slot <= Practice.commandArray.length){
					PracticeRunnable practiceCommand = (PracticeRunnable) Practice.commandArray[slot];
					practiceCommand.run(player);
				}
			}
		}
	}
}
