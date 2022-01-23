package net.danh.gang.Manager;

import net.danh.gang.Files.Files;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gangs {


    public static HashMap<String, Gangs> managerGang = new HashMap<>();
    public static HashMap<UUID, Gangs> playerGang = new HashMap<>();
    public static HashMap<UUID, Gangs> invite = new HashMap<>();
    private static Gangs instance;
    public List<UUID> gangPlayers = new ArrayList<>();
    public String name;
    public UUID owner;
    public Integer level;
    public Integer earnedxp = Integer.valueOf(0);

    public static Gangs getInstance() {

        if (instance == null) {
            instance = new Gangs();
        }
        return instance;
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
        pGang.owner = UUID.fromString(Files.getInstance().getdata().getString("gangs." + name + ".owner"));
        List<?> list = Files.getInstance().getdata().getList("gangs." + name + ".members");
        List<String> lists = (List) list;
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

}
