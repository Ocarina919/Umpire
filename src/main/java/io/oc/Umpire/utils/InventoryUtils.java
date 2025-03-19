package io.oc.Umpire.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {
    public static void addOrReplace(Inventory inventory, ItemStack itemStack){
        Material material = itemStack.getType();
        if(inventory.contains(material)) {
            inventory.remove(material);
        }
        inventory.addItem(itemStack);
    }
}
