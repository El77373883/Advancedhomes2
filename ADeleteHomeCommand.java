package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ADeleteHomeCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public ADeleteHomeCommand(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Solo jugadores pueden usar este comando.");
            return true;
        }

        if (!player.hasPermission("advancedhomes.use")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-permission"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(plugin.getPrefix() + AdvancedHomesPlugin.colorize(
                    "&cUso: /adeletehome <nombre>"));
            return true;
        }

        String homeName = args[0].toLowerCase();
        boolean deleted = plugin.getHomeManager().deleteHome(player.getUniqueId(), homeName);

        if (deleted) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-deleted", "{home}", homeName));
        } else {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-not-found", "{home}", homeName));
        }

        return true;
    }
}
