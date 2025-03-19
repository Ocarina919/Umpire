package io.oc.Umpire.practice;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import java.util.Arrays;

public class Practice {
	static final int INVENTORY_SIZE = 27;

	public static Inventory practiceInventory;
	public static PracticeCommand[] commandArray = new PracticeCommand[INVENTORY_SIZE];
	
	//Game Mode Controls

	public static void init(){
		practiceInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.BOLD + "Umpire Practice");
		for (int i = 0; i < INVENTORY_SIZE; i++){
			addCommand(PracticeCommands.blank, i);
		}

		Arrays.fill(commandArray, PracticeCommands.blank);

		addCommand(PracticeCommands.survivalMode, 0,0);
		addCommand(PracticeCommands.creativeMode, 0,1);
		addCommand(PracticeCommands.spectatorMode, 0,2);

		addCommand(PracticeCommands.lowArmor, 1,0);
		addCommand(PracticeCommands.mediumArmor, 1,1);
		addCommand(PracticeCommands.highArmor, 1,2);

		addCommand(PracticeCommands.lowTools, 2,0);
		addCommand(PracticeCommands.mediumTools, 2,1);
		addCommand(PracticeCommands.highTools, 2,2);

		addCommand(PracticeCommands.arrows, 3,0);
		addCommand(PracticeCommands.exp, 3,1);
		addCommand(PracticeCommands.tnt, 3,2);

		addCommand(PracticeCommands.saveInventory, 4,0);
		addCommand(PracticeCommands.loadInventory, 4,1);
		addCommand(PracticeCommands.clearSavedInventory, 4,2);

		addCommand(PracticeCommands.setDay, 5,0);
		addCommand(PracticeCommands.setNight, 5,1);
		addCommand(PracticeCommands.freezeTime, 5,2);

		addCommand(PracticeCommands.nightVision,8,0);
		addCommand(PracticeCommands.restore, 8,2);

	}

	public static void addCommand(PracticeCommand command, int x_pos, int y_pos){
		addCommand(command, x_pos + 9*y_pos);
	}

	public static void addCommand(PracticeCommand command, int index){
		commandArray[index] = command;

		ItemStack itemStack = new ItemStack(command.inventoryIcon,1);
		ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(command.description);
		itemMeta.setLore(Lists.newArrayList(command.lore));
		itemMeta.setMaxStackSize(1);
		itemStack.setItemMeta(itemMeta);

		practiceInventory.setItem(index, itemStack);
	}
}
