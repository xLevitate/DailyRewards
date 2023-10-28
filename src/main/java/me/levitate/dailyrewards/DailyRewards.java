package me.levitate.dailyrewards;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import me.levitate.dailyrewards.commands.MainCommand;
import me.levitate.dailyrewards.config.Configuration;
import me.levitate.dailyrewards.data.StorageService;
import me.levitate.dailyrewards.service.PlayerService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

public final class DailyRewards extends JavaPlugin {
    private StorageService storageService;
    private PlayerService playerService;
    private Configuration configuration;
    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();

        final PaperCommandManager commandManager = new PaperCommandManager(this);

        // Initialize the storage service.
        storageService = new StorageService(this);

        // Initialize the configuration class.
        configuration = new Configuration(this, getConfig());

        // Initialize the player service
        playerService = new PlayerService(storageService, configuration);

        // Register the main command.
        commandManager.registerCommand(new MainCommand(playerService, configuration));
    }

    @Override
    public void onDisable() {
        storageService.save();
    }
}
