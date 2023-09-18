package me.levitate.dailyrewards.service;

import lombok.AllArgsConstructor;
import me.levitate.dailyrewards.config.Configuration;
import me.levitate.dailyrewards.data.StorageService;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

@AllArgsConstructor
public class PlayerService {
    private final StorageService storageService;
    private final Configuration configuration;

    public void giveReward(Player player) {
        if (!player.isOnline()) return;

        UUID playerUUID = player.getUniqueId();
        Long time = System.currentTimeMillis() + Duration.ofHours(24).toMillis();

        if (!storageService.getRewardCooldown().containsKey(playerUUID)) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

            for (String s : configuration.getCommands()) {
                String command = s.replace("%player%", player.getName());
                Bukkit.dispatchCommand(console, command);
            }

            storageService.getRewardCooldown().put(playerUUID, time);
            configuration.sendMessage(player, configuration.getMessages().get("reward"));
        }
        else {
            configuration.sendMessage(player, configuration.getMessages().get("cooldown"));
        }
    }

    public void resetCooldown(Player sender, Player player) {
        if (!player.isOnline()) {
            configuration.sendMessage(sender, configuration.getMessages().get("offline"));
            return;
        }

        final UUID playerUUID = player.getUniqueId();

        if (!storageService.getRewardCooldown().containsKey(playerUUID)) {
            configuration.sendMessage(sender, configuration.getMessages().get("no-cooldown"));
        }
        else {
            storageService.getRewardCooldown().remove(playerUUID);
            configuration.sendMessage(sender, configuration.getMessages().get("reset").replace("%player%", player.getName()));
        }
    }
}
