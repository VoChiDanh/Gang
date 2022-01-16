package net.danh.gang.Events;

import net.danh.gang.Files.Files;
import net.danh.gang.Manager.Gangs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class Events implements Listener {
    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (e.getInventory().getType().equals(InventoryType.CHEST) &&
                e.getInventory().getTitle().equals(ChatColor.DARK_GRAY + "Thành Viên Của Gang"))
            e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        List<String> items = Files.getInstance().getconfig().getStringList("Break");
        if (Gangs.inGang(p)) {
            if (items.equals(e.getBlock().getType())) {
                Gangs pGang = Gangs.getGang(p);
                pGang.addXP(Files.getInstance().getconfig().getInt("Break." + e.getBlock().getType()));
            }
        }
    }
}

