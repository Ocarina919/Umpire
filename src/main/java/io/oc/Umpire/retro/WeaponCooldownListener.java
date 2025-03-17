package io.oc.Umpire.retro;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import static org.bukkit.Bukkit.getLogger;

import java.util.Collection;


public class WeaponCooldownListener implements Listener {

    @EventHandler
    public void onWeaponCooldown(PlayerInteractEvent event){
        ItemStack item = event.getItem();
        if (item != null){
            ItemMeta metadata = event.getItem().getItemMeta();
            if (metadata != null){
                Collection<AttributeModifier> attackSpeed = metadata.getAttributeModifiers(Attribute.ATTACK_SPEED);
                if (attackSpeed != null){
                    for (AttributeModifier as : attackSpeed){
                        getLogger().info(String.valueOf(as.getAmount()));
                    }
                }
                else{
                    AttributeModifier am = new AttributeModifier(NamespacedKey.minecraft("mykey"), 1000, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.MAINHAND);
                    metadata.addAttributeModifier(Attribute.ATTACK_SPEED, am);
                    item.setItemMeta(metadata);
                    //metadata.addAttributeModifier(Attribute.ATTACK_SPEED, At);
                    getLogger().info("attackSpeed is null");
                }
            }
            else{
                getLogger().info("metadata is null");
            }
        }
        else{
            getLogger().info("item is null");
        }
    }

    @EventHandler
    public void onEntityDamaged(EntityDamageByEntityEvent event){
        getLogger().info("Damage dealt: " + event.getDamage());
    }
}
