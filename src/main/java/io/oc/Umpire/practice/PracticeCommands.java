package io.oc.Umpire.practice;

import io.oc.Umpire.Umpire;
import io.oc.Umpire.core.UmpirePlayer;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static io.oc.Umpire.utils.InventoryUtils.addOrReplace;

public class PracticeCommands{
    public static SpectatorModeCommand spectatorMode = new SpectatorModeCommand();
    public static SurvivalModeCommand survivalMode = new SurvivalModeCommand();
    public static CreativeModeCommand creativeMode = new CreativeModeCommand();

    public static LowArmorCommand lowArmor = new LowArmorCommand();
    public static MediumArmorCommand mediumArmor = new MediumArmorCommand();
    public static HighArmorCommand highArmor = new HighArmorCommand();

    public static LowToolsCommand lowTools = new LowToolsCommand();
    public static MediumToolsCommand mediumTools = new MediumToolsCommand();
    public static HighToolsCommand highTools = new HighToolsCommand();

    public static ArrowsCommand arrows = new ArrowsCommand();
    public static ExpCommand exp = new ExpCommand();
    public static TNTCommand tnt = new TNTCommand();

    public static SaveInventoryCommand saveInventory = new SaveInventoryCommand();
    public static LoadInventoryCommand loadInventory = new LoadInventoryCommand();
    public static ClearSavedInventoryCommand clearSavedInventory = new ClearSavedInventoryCommand();

    public static SetDayCommand setDay = new SetDayCommand();
    public static SetNightCommand setNight = new SetNightCommand();
    public static FreezeTimeCommand freezeTime = new FreezeTimeCommand();

    public static RestoreCommand restore = new RestoreCommand();
    public static NightVisionCommand nightVision = new NightVisionCommand();
    public static BlankCommand blank = new BlankCommand();
}

class SurvivalModeCommand extends PracticeCommand implements PracticeRunnable {
    public SurvivalModeCommand(){
        super(Material.OAK_SAPLING,
                "Survival Mode",
                "Sets the player's gamemode to Survival"
        );
    }
    public void run(Player runner) {
        runner.setGameMode(GameMode.SURVIVAL);
    }
}

class CreativeModeCommand extends PracticeCommand implements PracticeRunnable {
    public CreativeModeCommand(){
        super(Material.COMMAND_BLOCK,
                "Creative Mode",
                "Sets the player's gamemode to Creative"
        );
    }
    public void run(Player runner) {
        runner.setGameMode(GameMode.CREATIVE);
    }
}

class SpectatorModeCommand extends PracticeCommand implements PracticeRunnable {
    public SpectatorModeCommand(){
        super(Material.SPYGLASS,
                "Spectator Mode",
                "Sets the player's gamemode to Spectator"
        );
    }
    public void run(Player runner) {
        runner.setGameMode(GameMode.SPECTATOR);
    }
}

class LowArmorCommand extends PracticeCommand implements PracticeRunnable {
    public LowArmorCommand(){
        super(Material.LEATHER_CHESTPLATE,
                "Leather Armor",
                "Gives the player the leather kit"
        );
    }
    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();

        inventory.setHelmet(new ItemStack(Material.LEATHER_HELMET));
        inventory.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.LEATHER_BOOTS));

        addOrReplace(inventory, new ItemStack(Material.STONE_SWORD));
        inventory.remove(Material.IRON_SWORD);
        inventory.remove(Material.DIAMOND_SWORD);
        addOrReplace(inventory, new ItemStack(Material.BOW));
        addOrReplace(inventory, new ItemStack(Material.ARROW,64));
        
    }
}

class MediumArmorCommand extends PracticeCommand implements PracticeRunnable {
    public MediumArmorCommand() {
        super(Material.IRON_CHESTPLATE,
                "Iron Armor",
                "Gives the player the iron kit"
        );
    }

    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();
        
        inventory.setHelmet(new ItemStack(Material.IRON_HELMET));
        inventory.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.IRON_BOOTS));

        addOrReplace(inventory, new ItemStack(Material.IRON_SWORD));
        inventory.remove(Material.STONE_SWORD);
        inventory.remove(Material.DIAMOND_SWORD);
        addOrReplace(inventory, new ItemStack(Material.BOW));
        addOrReplace(inventory, new ItemStack(Material.ARROW,64));
    }
}

class HighArmorCommand extends PracticeCommand implements PracticeRunnable {
    public HighArmorCommand() {
        super(Material.DIAMOND_CHESTPLATE,
                "Diamond Armor",
                "Gives the player the diamond kit"
        );
    }

    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();

        inventory.setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        inventory.setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        inventory.setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        inventory.setBoots(new ItemStack(Material.DIAMOND_BOOTS));

        addOrReplace(inventory, new ItemStack(Material.DIAMOND_SWORD));
        inventory.remove(Material.STONE_SWORD);
        inventory.remove(Material.IRON_SWORD);
        addOrReplace(inventory, new ItemStack(Material.BOW));
        addOrReplace(inventory, new ItemStack(Material.ARROW,64));
    }
}

class LowToolsCommand extends PracticeCommand implements PracticeRunnable{
    public LowToolsCommand() {
        super(Material.STONE_PICKAXE,
                "Basic Tools",
                "Gives the player basic tools");
    }

    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();

        addOrReplace(inventory, new ItemStack(Material.STONE_PICKAXE));
        inventory.remove(Material.IRON_PICKAXE);
        inventory.remove(Material.DIAMOND_PICKAXE);

        addOrReplace(inventory, new ItemStack(Material.STONE_AXE));
        inventory.remove(Material.IRON_AXE);
        inventory.remove(Material.DIAMOND_AXE);

        addOrReplace(inventory, new ItemStack(Material.STONE_SHOVEL));
        inventory.remove(Material.IRON_SHOVEL);
        inventory.remove(Material.DIAMOND_SHOVEL);

        addOrReplace(inventory, new ItemStack(Material.COOKED_PORKCHOP, 64));
        addOrReplace(inventory, new ItemStack(Material.PINK_CONCRETE, 64));
    }
}

class MediumToolsCommand extends PracticeCommand implements PracticeRunnable{
    public MediumToolsCommand() {
        super(Material.IRON_PICKAXE,
                "Standard Tools",
                "Gives the player standard tools"
        );
    }

    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();

        inventory.remove(Material.STONE_PICKAXE);
        addOrReplace(inventory, new ItemStack(Material.IRON_PICKAXE));
        inventory.remove(Material.DIAMOND_PICKAXE);

        inventory.remove(Material.STONE_AXE);
        addOrReplace(inventory, new ItemStack(Material.IRON_AXE));
        inventory.remove(Material.DIAMOND_AXE);

        inventory.remove(Material.STONE_SHOVEL);
        addOrReplace(inventory, new ItemStack(Material.IRON_SHOVEL));
        inventory.remove(Material.DIAMOND_SHOVEL);

        addOrReplace(inventory, new ItemStack(Material.WATER_BUCKET));
        addOrReplace(inventory, new ItemStack(Material.COOKED_PORKCHOP, 64));
        addOrReplace(inventory, new ItemStack(Material.PINK_CONCRETE, 64));
    }
}

class HighToolsCommand extends PracticeCommand implements PracticeRunnable{
    public HighToolsCommand() {
        super(Material.DIAMOND_PICKAXE,
                "Improved Tools",
                "Gives the player Improved tools"
        );
    }

    public void run(Player runner) {
        PlayerInventory inventory = runner.getInventory();

        inventory.remove(Material.STONE_PICKAXE);
        inventory.remove(Material.IRON_PICKAXE);
        addOrReplace(inventory, new ItemStack(Material.DIAMOND_PICKAXE));

        inventory.remove(Material.STONE_AXE);
        inventory.remove(Material.IRON_AXE);
        addOrReplace(inventory, new ItemStack(Material.DIAMOND_AXE));

        inventory.remove(Material.STONE_SHOVEL);
        inventory.remove(Material.IRON_SHOVEL);
        addOrReplace(inventory, new ItemStack(Material.DIAMOND_SHOVEL));

        addOrReplace(inventory, new ItemStack(Material.TORCH, 64));
        addOrReplace(inventory, new ItemStack(Material.WATER_BUCKET));
        addOrReplace(inventory, new ItemStack(Material.COOKED_PORKCHOP, 64));
        addOrReplace(inventory, new ItemStack(Material.PINK_CONCRETE, 64));
    }
}

class ArrowsCommand extends PracticeCommand implements PracticeRunnable{
    public ArrowsCommand() {
        super(Material.ARROW,
                "Give Arrows",
                "Gives the player another stack of Arrows"
        );
    }

    public void run(Player runner) {
        runner.getInventory().addItem(new ItemStack(Material.ARROW,64));
    }
}

class ExpCommand extends PracticeCommand implements PracticeRunnable{
    public ExpCommand() {
        super(Material.EXPERIENCE_BOTTLE,
                "Echanting Tools",
                "Gives the players the tools to enchant their gear"
        );
    }

    public void run(Player runner) {
        Inventory inventory = runner.getInventory();
        
        runner.setLevel(20);

        if(! inventory.contains(Material.ENCHANTING_TABLE)) {
            inventory.addItem(new ItemStack(Material.ENCHANTING_TABLE));
        }
        addOrReplace(inventory, new ItemStack(Material.LAPIS_LAZULI, 32));
    }
}

class TNTCommand extends PracticeCommand implements PracticeRunnable{
    public TNTCommand() {
        super(Material.TNT,
                "TNT Kit",
                "Gives the player the necessary supplies to cannon"
        );
    }

    public void run(Player runner) {
        Inventory inventory = runner.getInventory();

        addOrReplace(inventory, new ItemStack(Material.WATER_BUCKET));
        addOrReplace(inventory, new ItemStack(Material.STONE_BUTTON,32));
        addOrReplace(inventory, new ItemStack(Material.TNT,64));
        addOrReplace(inventory, new ItemStack(Material.ORANGE_CONCRETE,64));
        addOrReplace(inventory, new ItemStack(Material.LADDER,32));
        addOrReplace(inventory, new ItemStack(Material.REDSTONE,32));

    }
}

class SaveInventoryCommand extends PracticeCommand implements PracticeRunnable{
    public SaveInventoryCommand() {
        super(Material.HONEY_BLOCK,
                "Save Inventory",
                "Saves the player's inventory"
        );
    }

    public void run(Player runner) {
        UmpirePlayer up = Umpire.getPlayer(runner);
        up.saveArmor();
        up.saveInventory();
    }
}

class LoadInventoryCommand extends PracticeCommand implements PracticeRunnable{
    public LoadInventoryCommand() {
        super(Material.HONEY_BOTTLE,
                "Load Inventory",
                "Loads the Saved Inventory"
        );
    }

    public void run(Player runner) {
        UmpirePlayer up = Umpire.getPlayer(runner);
        up.loadInventory();
        up.loadArmor();
    }
}

class ClearSavedInventoryCommand extends PracticeCommand implements PracticeRunnable{
    public ClearSavedInventoryCommand() {
        super(Material.GLASS_BOTTLE,
                "Clear Saved Inventory",
                "Clears the Saved Inventory"
        );
    }

    public void run(Player runner) {
        UmpirePlayer up = Umpire.getPlayer(runner);
        up.clearArmor();
        up.clearInventory();
    }
}

class SetDayCommand extends PracticeCommand implements PracticeRunnable{
    public SetDayCommand() {
        super(Material.WAXED_COPPER_BULB,
                "Set Time to Day",
                "Sets the time to Day"
        );
    }

    public void run(Player runner) {
        runner.getWorld().setTime(6000);
    }
}

class SetNightCommand extends PracticeCommand implements PracticeRunnable{
    public SetNightCommand() {
        super(Material.WAXED_WEATHERED_COPPER_BULB,
                "Night Time",
                "Setsn the time of day to Night"
        );
    }

    public void run(Player runner) {
        runner.getWorld().setTime(18000);
    }
}

class FreezeTimeCommand extends PracticeCommand implements PracticeRunnable{
    public FreezeTimeCommand() {
        super(Material.CLOCK,
                "Toggle Daylight Cycle",
                "Toggles the Day/Night Cycle"
        );
    }

    public void run(Player runner) {
        if(runner.getWorld().getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE)) {
            runner.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        }else {
            runner.getWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        }
    }
}

class RestoreCommand extends PracticeCommand implements PracticeRunnable{
    public RestoreCommand() {
        super(Material.GOLDEN_APPLE,
                "Restore Player",
                "Heals and Feeds the player"
        );
    }

    public void run(Player runner) {
        runner.setHealth(runner.getAttribute(Attribute.MAX_HEALTH).getValue());
        runner.setFoodLevel(20);
        runner.setSaturation(5);
    }
}

class NightVisionCommand extends PracticeCommand implements PracticeRunnable{
    public NightVisionCommand() {
        super(Material.GOLDEN_CARROT,
                "Toggle Night Vision",
                "Toggles the Night Vision Potion Effect on the Player"
        );
    }

    public void run(Player runner) {
            runner.removePotionEffect(PotionEffectType.NIGHT_VISION);
            runner.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 9999999, 1));
    }
}

class BlankCommand extends PracticeCommand implements PracticeRunnable{
    public BlankCommand() {
        super(Material.RED_STAINED_GLASS_PANE,
        "",
                "There is no command associated"
        );
    }

    public void run(Player runner) {}
}