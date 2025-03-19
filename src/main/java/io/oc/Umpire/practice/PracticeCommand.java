package io.oc.Umpire.practice;

import org.bukkit.Material;

public class PracticeCommand {
    Material inventoryIcon;
    String description;
    String lore;
    public PracticeCommand(Material inventoryIcon, String description, String lore){
        this.inventoryIcon = inventoryIcon;
        this.description = description;
        this.lore = lore;
    }
}
