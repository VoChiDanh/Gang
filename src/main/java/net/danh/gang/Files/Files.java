package net.danh.gang.Files;

import net.danh.gang.Gang;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Files {
    private static Files instance;

    public static Files getInstance() {

        if (instance == null) {
            instance = new Files();
        }
        return instance;
    }


    private File configFile, languageFile, dataFile;
    private FileConfiguration config, language, data;


    public void createconfig() {
        configFile = new File(Gang.getInstance().getDataFolder(), "config.yml");
        languageFile = new File(Gang.getInstance().getDataFolder(), "language.yml");
        dataFile = new File(Gang.getInstance().getDataFolder(), "data.yml");

        if (!configFile.exists()) Gang.getInstance().saveResource("config.yml", false);
        if (!languageFile.exists()) Gang.getInstance().saveResource("language.yml", false);
        if (!dataFile.exists()) Gang.getInstance().saveResource("data.yml", false);
        config = new YamlConfiguration();
        language = new YamlConfiguration();
        data = new YamlConfiguration();

        try {
            config.load(configFile);
            language.load(languageFile);
            data.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getconfig() {
        return config;
    }

    public FileConfiguration getlanguage() {
        return language;
    }

    public FileConfiguration getdata() {
        return data;
    }

    public void reloadconfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        language = YamlConfiguration.loadConfiguration(languageFile);
    }

    public void saveconfig() {
        try {
            config.save(configFile);
        } catch (IOException ignored) {
        }
    }


    public void savelanguage() {
        try {
            language.save(languageFile);
        } catch (IOException ignored) {
        }
    }


    public void savedata() {
        try {
            data.save(dataFile);
        } catch (IOException ignored) {
        }
    }

    public String convert(String s) {
        return s.replaceAll("&", "§");
    }
}
