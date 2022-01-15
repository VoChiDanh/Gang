package net.danh.gang.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.danh.gang.Files.Files;
import net.danh.gang.Gang;
import net.danh.gang.Manager.Gangs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Commands implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length == 2) {
                        if (!Gangs.inGang(p).booleanValue()) {
                            long gangPrice = Files.getInstance().getconfig().getInt("settings.createmoney");
                            if (Gang.economy.getBalance((OfflinePlayer)p) >= gangPrice) {
                                Gang.economy.withdrawPlayer((OfflinePlayer)p, gangPrice);
                                Gangs.createGang(args[1], p);
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.gang_created").replaceAll("%gang_name%", args[1])));
                            } else {
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_enough_money")));
                            }
                        } else {
                            p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.in_gang")));
                        }
                    } else {
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_enough_create_args")));
                    }
                } else if (args[0].equalsIgnoreCase("kick")) {
                    if (args.length == 2) {
                        if (Gangs.inGang(p).booleanValue()) {
                            if (Gangs.isOwner(p).booleanValue()) {
                                Gangs pGang = Gangs.getGang(p);
                                OfflinePlayer kickedplayer = Bukkit.getOfflinePlayer(args[1]);
                                UUID kickeduuid = kickedplayer.getUniqueId();
                                if (kickeduuid != p.getUniqueId())
                                    if (Gangs.playerGang.containsKey(kickeduuid)) {
                                        Gangs.playerGang.remove(kickeduuid);
                                        pGang.gangPlayers.remove(kickeduuid);
                                        List<String> stringmembers = new ArrayList<>();
                                        for (UUID player : pGang.gangPlayers)
                                            stringmembers.add(player.toString());
                                        Files.getInstance().getdata().set("gangs." + pGang.name + ".members", stringmembers);
                                        Files.getInstance().savedata();
                                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.player_kicked")));
                                    } else {
                                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.player_not_gang")));
                                    }
                            } else {
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_gang_owner")));
                            }
                        } else {
                            p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_in_gang")));
                        }
                    } else {
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_enough_create_args")));
                    }
                } else if (args[0].equalsIgnoreCase("leave")) {
                    if (args.length == 2) {
                        if (Gangs.inGang(p).booleanValue()) {
                            if (!Gangs.isOwner(p).booleanValue()) {
                                UUID kickeduuid = p.getUniqueId();
                                Gangs.playerGang.remove(kickeduuid);
                                Gangs pGang = Gangs.getGang(p);
                                pGang.gangPlayers.remove(kickeduuid);
                                List<String> stringmembers = new ArrayList<>();
                                for (UUID player : pGang.gangPlayers)
                                    stringmembers.add(player.toString());
                                Files.getInstance().getdata().set("gangs." + pGang.name + ".members", stringmembers);
                                Files.getInstance().savedata();
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.left_gang")));
                            } else {
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.gang_owner")));
                            }
                        } else {
                            p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_in_gang")));
                        }
                    } else {
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_enough_create_args")));
                    }
                } else if (args[0].equalsIgnoreCase("invite")) {
                    if (args.length == 2) {
                        if (Gangs.inGang(p).booleanValue()) {
                            Gangs pGang = Gangs.getGang(p);
                            if (pGang.gangPlayers.size() < 15) {
                                Player invitedplayer = Bukkit.getPlayer(args[1]);
                                if (invitedplayer != null) {
                                    UUID inviteduuid = invitedplayer.getUniqueId();
                                    if (!Gangs.inGang(invitedplayer).booleanValue()) {
                                        Gangs.invite.put(inviteduuid, pGang);
                                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.invite_sent").replaceAll("%player%", invitedplayer.getName())));
                                        invitedplayer.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.gang_invite").replaceAll("%gangname%", pGang.name)));
                                    } else {
                                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.player_in_gang")));
                                    }
                                } else {
                                    p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_online")));
                                }
                            } else {
                                p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.many_players")));
                            }
                        } else {
                            p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_in_gang")));
                        }
                    } else {
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_enough_create_args")));
                    }
                } else if (args[0].equalsIgnoreCase("accept")) {
                    if (Gangs.invite.containsKey(p.getUniqueId())) {
                        Gangs pGang = (Gangs) Gangs.invite.get(p.getUniqueId());
                        Gangs.playerGang.put(p.getUniqueId(), pGang);
                        pGang.gangPlayers.add(p.getUniqueId());
                        List<String> stringmembers = new ArrayList<>();
                        for (UUID player : pGang.gangPlayers)
                            stringmembers.add(player.toString());
                        Files.getInstance().getdata().set("gangs." + pGang.name + ".members", stringmembers);
                        Files.getInstance().savedata();
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.joined_gang")));
                    }
                } else if (args[0].equalsIgnoreCase("member")) {
                    if (Gangs.playerGang.containsKey(p.getUniqueId())) {
                        Gangs pGang = (Gangs) Gangs.playerGang.get(p.getUniqueId());
                        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_GRAY + "Gangs Members");
                        ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);
                        ItemMeta glassMeta = glass.getItemMeta();
                        glassMeta.setDisplayName(" ");
                        glass.setItemMeta(glassMeta);
                        for (int x = 0; x < inv.getSize(); x++) {
                            if (inv.getItem(x) == null)
                                inv.setItem(x, glass);
                        }
                        SkullMeta metas = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
                        metas.setOwner("MHF_Question");
                        metas.setDisplayName(" ");
                        ItemStack stacks = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                        stacks.setItemMeta((ItemMeta)metas);
                        int i;
                        for (i = 1; i <= 15; i++) {
                            switch (i) {
                                case 1:
                                    inv.setItem(2, stacks);
                                    break;
                                case 2:
                                    inv.setItem(3, stacks);
                                    break;
                                case 3:
                                    inv.setItem(4, stacks);
                                    break;
                                case 4:
                                    inv.setItem(5, stacks);
                                    break;
                                case 5:
                                    inv.setItem(6, stacks);
                                    break;
                                case 6:
                                    inv.setItem(11, stacks);
                                    break;
                                case 7:
                                    inv.setItem(12, stacks);
                                    break;
                                case 8:
                                    inv.setItem(13, stacks);
                                    break;
                                case 9:
                                    inv.setItem(14, stacks);
                                    break;
                                case 10:
                                    inv.setItem(15, stacks);
                                    break;
                                case 11:
                                    inv.setItem(20, stacks);
                                    break;
                                case 12:
                                    inv.setItem(21, stacks);
                                    break;
                                case 13:
                                    inv.setItem(22, stacks);
                                    break;
                                case 14:
                                    inv.setItem(23, stacks);
                                    break;
                                case 15:
                                    inv.setItem(24, stacks);
                                    break;
                            }
                        }
                        i = 1;
                        for (UUID player : pGang.gangPlayers) {
                            SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
                            meta.setOwner(Bukkit.getOfflinePlayer(player).getName());
                            ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
                            stack.setItemMeta((ItemMeta)meta);
                            switch (i) {
                                case 1:
                                    inv.setItem(2, stack);
                                    break;
                                case 2:
                                    inv.setItem(3, stack);
                                    break;
                                case 3:
                                    inv.setItem(4, stack);
                                    break;
                                case 4:
                                    inv.setItem(5, stack);
                                    break;
                                case 5:
                                    inv.setItem(6, stack);
                                    break;
                                case 6:
                                    inv.setItem(11, stack);
                                    break;
                                case 7:
                                    inv.setItem(12, stack);
                                    break;
                                case 8:
                                    inv.setItem(13, stack);
                                    break;
                                case 9:
                                    inv.setItem(14, stack);
                                    break;
                                case 10:
                                    inv.setItem(15, stack);
                                    break;
                                case 11:
                                    inv.setItem(20, stack);
                                    break;
                                case 12:
                                    inv.setItem(21, stack);
                                    break;
                                case 13:
                                    inv.setItem(22, stack);
                                    break;
                                case 14:
                                    inv.setItem(23, stack);
                                    break;
                                case 15:
                                    inv.setItem(24, stack);
                                    break;
                            }
                            i++;
                        }
                        p.openInventory(inv);
                    } else {
                        p.sendMessage(Files.getInstance().convert(Files.getInstance().getlanguage().getString("messages.not_in_gang")));
                    }
                }
            } else {
                for (String helpplayer : Files.getInstance().getlanguage().getStringList("messages.unknown_command"))
                    p.sendMessage(Files.getInstance().convert(helpplayer));
            }
        } else {
            sender.sendMessage("Gang > Console commands are not supported, please execute commands as a Player.");
            return true;
        }
        return false;
    }
}
