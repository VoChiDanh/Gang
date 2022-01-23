package net.danh.gang.Events;

import net.danh.gang.Files.Files;
import net.danh.gang.Manager.Gangs;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Death implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent e) {
        if (Gangs.inGang(e.getEntity().getPlayer())) {
            Files.getInstance().removeXP(e.getEntity().getPlayer(), 50);
            if (Gangs.isOwner(e.getEntity().getPlayer())) {
                Files.getInstance().removeXP(e.getEntity().getPlayer(), 100);
            }
        }
    }
}
