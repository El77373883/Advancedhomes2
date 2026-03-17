package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ASetHomeCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public ASetHomeCommand(AdvancedHomesPlugin plugin) {
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
                    "&cUso: /asethome <nombre>"));
            return true;
        }

        String homeName = args[0].toLowerCase();

        // Validar nombre (solo letras, numeros, guiones)
        if (!homeName.matches("[a-zA-Z0-9_\\-]+")) {
            player.sendMessage(plugin.getPrefix() + AdvancedHomesPlugin.colorize(
                    "&cEl nombre solo puede contener letras, numeros, _ y -"));
            return true;
        }

        plugin.getHomeManager().setHome(player.getUniqueId(), homeName, player.getLocation());
        player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-set", "{home}", homeName));

        return true;
    }
}
