package me.levitate.dailyrewards.config;

import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    private final Plugin plugin;
    private FileConfiguration fileConfiguration;

    @Getter
    private List<String> commands;

    @Getter
    private final Map<String, String> messages = new LinkedHashMap<>();

    public Configuration(Plugin plugin, FileConfiguration fileConfiguration) {
        this.plugin = plugin;
        this.fileConfiguration = fileConfiguration;

        reloadConfig();
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(MiniMessage.miniMessage().deserialize(message));
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        plugin.saveDefaultConfig();

        this.fileConfiguration = plugin.getConfig();
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveConfig();

        messages.clear();

        this.commands = fileConfiguration.getStringList("commands");

        for (String s : fileConfiguration.getConfigurationSection("messages").getKeys(false)) {
            messages.put(s, fileConfiguration.getString("messages." + s));
        }
    }
}
