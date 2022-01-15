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

        if (identifier.equalsIgnoreCase("gangname")) {
            if (Gangs.getInstance().playerGang.containsKey(p.getUniqueId()))
                return ((Gangs) Gangs.getInstance().playerGang.get(p.getUniqueId())).name;
            return "";
        }
        if (identifier.equalsIgnoreCase("xpneeded")) {
            if (Gangs.getInstance().inGang(p).booleanValue()) {
                Gangs pGang = Gangs.getInstance().getGang(p);
                if (pGang.level.intValue() < Files.getInstance().getconfig().getInt("settings.maxlevel")) {
                    int nextlevel = pGang.level.intValue() + 1;
                    int nextlevelxp = Files.getInstance().getconfig().getInt("levels.level" + nextlevel);
                    return String.valueOf(nextlevelxp - pGang.earnedxp.intValue());
                }
                return "";
            }
            return "";
        }
        if (identifier.equalsIgnoreCase("level") &&
                Gangs.getInstance().inGang(p).booleanValue())
            return String.valueOf((Gangs.getInstance().getGang(p)).level);
        return null;
    }

}