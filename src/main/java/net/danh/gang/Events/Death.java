package net.danh.gang.Events;

import net.danh.gang.Manager.Gangs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e){
        if (Gangs.inGang(e.getEntity().getPlayer())){
            Gangs.getGang(e.getEntity().getPlayer()).removeXP(100);
        }
    }
}
