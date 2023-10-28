package me.levitate.dailyrewards.data;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StorageService {
    // Holds the cooldown for the player to claim another reward.
    @Getter
    private final HashMap<UUID, Long> rewardCooldown = new HashMap<>();

    private final File file;

    @Getter
    private FileConfiguration fileConfiguration;

    public StorageService(Plugin plugin) {
        file = new File(plugin.getDataFolder(), "data.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                System.out.println("Failed to create storage file.");
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);

        // Load all the players.
        if (fileConfiguration.contains("players")) {
            fileConfiguration.getConfigurationSection("players").getKeys(false).forEach(uuidStr -> {
                UUID uuid = UUID.fromString(uuidStr);
                Long value = fileConfiguration.getLong("players." + uuidStr);
                rewardCooldown.put(uuid, value);
            });
        }

        // Save data every 5 minutes.
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, 6000L, 6000L );
    }

    public boolean onCooldown(UUID uuid) {
        Long cooldown = rewardCooldown.get(uuid);

        if (cooldown - System.currentTimeMillis() <= 0) {
            rewardCooldown.remove(uuid);
            return false;
        }

        return true;
    }

    public void save() {
        fileConfiguration.set("players", null);
        rewardCooldown.forEach((uuid, value) -> fileConfiguration.set("players." + uuid.toString(), value));
        rewardCooldown.clear();

        try {
            fileConfiguration.save(file);
        }
        catch (IOException e) {
            System.out.println("Couldn't save storage file.");
        }
    }

    public void reload() {
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }
}
