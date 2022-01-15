package net.danh.gang;

import net.danh.gang.Commands.Commands;
import net.danh.gang.Events.Events;
import net.danh.gang.Files.Files;
import net.danh.gang.Hook.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Gang extends JavaPlugin {


    private static Gang instance;

    public static Economy economy;

    public static Gang getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getCommand("gang").setExecutor(new Commands());
        getServer().getPluginManager().registerEvents(new Events(), this);
        Files.getInstance().createconfig();

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().log(Level.INFO, "Hooked onto PlaceholderAPI");
            new PlaceholderAPI().register();
        } else {
            getServer().getPluginManager().disablePlugin(this);
        }
        if (getServer().getPluginManager().isPluginEnabled("Vault")) {
            getLogger().log(Level.INFO, "Hooked onto Vault");
        } else {
            getServer().getPluginManager().disablePlugin(this);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null)
            economy = (Economy)economyProvider.getProvider();
        return (economy != null);
    }
}