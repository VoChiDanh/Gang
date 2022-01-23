package net.danh.gang.Hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.gang.Files.Files;
import net.danh.gang.Gang;
import net.danh.gang.Manager.Gangs;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {

    private static PlaceholderAPI instance;

    public static PlaceholderAPI getInstance() {

        if (instance == null) {
            instance = new PlaceholderAPI();
        }
        return instance;
    }


    @Override
    public String getAuthor() {
        return Gang.getInstance().getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier() {
        return "gang";
    }

    @Override
    public String getVersion() {
        return Gang.getInstance().getDescription().getVersion();
    }


    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "Player not online";
        }

        if (identifier.equalsIgnoreCase("name")) {
            if (Gangs.playerGang.containsKey(p.getUniqueId()))
                return ((Gangs) Gangs.playerGang.get(p.getUniqueId())).name;
            return "";
        }
        if (identifier.equalsIgnoreCase("xpmax")) {
            if (Gangs.inGang(p).booleanValue()) {
                return String.valueOf(Files.getInstance().getLevel(p) * 1000);
            }
            return "";
        }
        if (identifier.equalsIgnoreCase("xp")) {
            if (Gangs.inGang(p).booleanValue()) {
                return String.valueOf(Files.getInstance().getXP(p));
            }
            return "";
        }
        if (identifier.equalsIgnoreCase("level")) {
            if (Gangs.inGang(p).booleanValue()) {
                return String.valueOf(Files.getInstance().getLevel(p));
            }
            return "";
        }
        return null;
    }
}