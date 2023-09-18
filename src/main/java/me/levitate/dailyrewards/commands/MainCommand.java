package me.levitate.dailyrewards.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import lombok.AllArgsConstructor;
import me.levitate.dailyrewards.config.Configuration;
import me.levitate.dailyrewards.service.PlayerService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("daily|dailyrewards|dailyreward|dr")
@AllArgsConstructor
public class MainCommand extends BaseCommand {
    private final PlayerService playerService;
    private final Configuration configuration;

    @Default
    @CommandPermission("daily.use")
    public void onReward(CommandSender sender) {
        final Player player = (Player) sender;

        playerService.giveReward(player);
    }

    @Subcommand("reload")
    @CommandPermission("daily.reload")
    public void onReset(CommandSender sender) {
        configuration.reloadConfig();
    }

    @Subcommand("reset")
    @Syntax("<player>")
    @CommandPermission("daily.reset")
    @CommandCompletion("@players")
    public void onReset(CommandSender sender, OnlinePlayer onlinePlayer) {
        final Player player = (Player) sender;
        final Player target = onlinePlayer.player;

        playerService.resetCooldown(player, target);
    }
}
