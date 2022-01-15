package net.danh.gang.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import net.danh.gang.Files.Files;
import net.danh.gang.Gang;
import net.danh.gang.Manager.Gangs;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Gangs {


    private static Gangs instance;


    public static Gangs getInstance(){

        if (instance == null) {
            instance = new Gangs();
        }
        return instance;
    }
    public static HashMap<String, Gangs> managerGang = new HashMap<>();

    public static HashMap<UUID, Gangs> playerGang = new HashMap<>();

    public static HashMap<UUID, Gangs> invite = new HashMap<>();

    public List<UUID> gangPlayers = new ArrayList<>();

    public String name;

    public Location location;

    public UUID owner;

    public Integer level;

    public Integer earnedxp = Integer.valueOf(0);

    public void addXP(int xp) {
        if (this.level.intValue() < Files.getInstance().getconfig().getInt("settings.maxlevel")) {
            this.earnedxp = Integer.valueOf(this.earnedxp.intValue() + xp);
            int nextlevel = this.level.intValue() + 1;
            if (this.earnedxp.intValue() >= Files.getInstance().getconfig().getInt("levels.level" + String.valueOf(nextlevel))) {
                this.earnedxp = Integer.valueOf(0);
                this.level = Integer.valueOf(nextlevel);
                Files.getInstance().getdata().set("gangs." + this.name + ".level", String.valueOf(this.level));
                Files.getInstance().getdata().set("gangs." + this.name + ".xp", String.valueOf(this.earnedxp));
                Files.getInstance().saveconfig();
            } else {
                Files.getInstance().getdata().set("gangs." + this.name + ".xp", String.valueOf(this.earnedxp));
                Files.getInstance().saveconfig();
            }
        }
    }

    public static Gangs getGang(Player p) {
        return playerGang.get(p.getUniqueId());
    }

    public static Boolean isOwner(Player p) {
        if (inGang(p).booleanValue() &&
                (getGang(p)).owner.equals(p.getUniqueId()))
            return Boolean.valueOf(true);
        return Boolean.valueOf(false);
    }

    public static Boolean inGang(Player p) {
        return Boolean.valueOf(playerGang.containsKey(p.getUniqueId()));
    }

    public static void initializeGang(String name) {
        Gangs pGang = new Gangs();
        pGang.name = name;
        pGang.level = Integer.valueOf(Files.getInstance().getdata().getInt("gangs." + name + ".level"));
        pGang.earnedxp = Integer.valueOf(Files.getInstance().getdata().getInt("gangs." + name + ".xp"));
        if (Files.getInstance().getdata().contains("gangs." + name + ".home"))
            pGang.location = getLocation(Files.getInstance().getdata().getString("gangs." + name + ".home"));
        pGang.owner = UUID.fromString(Files.getInstance().getdata().getString("gangs." + name + ".owner"));
        List<?> list = Files.getInstance().getdata().getList("gangs." + name + ".members");
        List<String> lists = (List)list;
        pGang.gangPlayers = new ArrayList<>();
        for (String player : lists)
            pGang.gangPlayers.add(UUID.fromString(player));
        for (UUID player : pGang.gangPlayers)
            playerGang.put(player, pGang);
        managerGang.put(name, pGang);
    }


    public static void createGang(String name, Player p) {
        List<UUID> members = new ArrayList<>();
        Files.getInstance().getdata().set("gangs." + name + ".owner", p.getUniqueId().toString());
        members.add(p.getUniqueId());
        List<String> stringmembers = new ArrayList<>();
        for (UUID player : members)
            stringmembers.add(player.toString());
        Files.getInstance().getdata().set("gangs." + name + ".members", stringmembers);
        Files.getInstance().getdata().set("gangs." + name + ".level", Integer.valueOf(1));
        Files.getInstance().getdata().set("gangs." + name + ".xp", Integer.valueOf(0));
        Files.getInstance().savedata();
        initializeGang(name);
    }

    public static Location getLocation(String s) {
        if (s == null || s.trim() == "")
            return null;
        String[] parts = s.split(":");
        if (parts.length == 4) {
            World w = Bukkit.getServer().getWorld(parts[0]);
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(w, x, y, z);
        }
        return null;
    }


    public static String getStringLocation(Location loc) {
        if (loc == null)
            return "";
        return loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
    }
}
