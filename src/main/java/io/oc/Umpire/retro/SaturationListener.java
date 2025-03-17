package io.oc.Umpire.retro;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExhaustionEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import static org.bukkit.event.entity.EntityExhaustionEvent.ExhaustionReason.REGEN;

public class SaturationListener implements Listener {

    @EventHandler
    public void onSatiatedHeal(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player){
            if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED){
                //getLogger().info("Replacing regen amount " + event.getAmount() + " with 1.0f");
                event.setAmount(1.0f);
            }
        }
    }

    @EventHandler
    public void onExhaustionEvent(EntityExhaustionEvent event){
        //getLogger().info("Exhaustion event of type " + event.getExhaustionReason() + " with value " + event.getExhaustion());

        if(event.getExhaustionReason() == REGEN){
            event.setExhaustion(4.0f);
            //getLogger().info("Setting reg exhaustion to 4.0f");
        }
    }
}
