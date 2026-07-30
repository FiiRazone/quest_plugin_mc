package me.FiRazone.quest.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {

    private final JavaPlugin plugin;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public File getPlayerFile(UUID uuid) {
        File folder = new File(plugin.getDataFolder(), "players");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        return new File(folder, uuid + ".yml");
    }

    public YamlConfiguration getPlayerData(UUID uuid) {
        File file = getPlayerFile(uuid);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void savePlayerData(UUID uuid, YamlConfiguration data) {
        try {
            data.save(getPlayerFile(uuid));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}