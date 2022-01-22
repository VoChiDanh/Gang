package net.danh.gang.Events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class Events implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.CHEST) &&
                e.getInventory().getTitle().equals(ChatColor.DARK_GRAY + "Thành Viên Của Gang"))
            e.setCancelled(true);
    }
}

