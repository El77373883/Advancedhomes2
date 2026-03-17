package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import com.advancedhomes.gui.HomeGUI;
import com.advancedhomes.managers.CooldownManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHomeCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public AHomeCommand(AdvancedHomesPlugin plugin) {
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

        // Sin argumentos: abrir GUI
        if (args.length == 0) {
            if (plugin.getHomeManager().getHomes(player.getUniqueId()).isEmpty()) {
                player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-homes"));
                return true;
            }
            new HomeGUI(plugin).openGUI(player);
            return true;
        }

        String homeName = args[0].toLowerCase();
        CooldownManager cooldownManager = plugin.getCooldownManager();

        if (cooldownManager.isOnCooldown(player.getUniqueId())
                && !player.hasPermission("advancedhomes.cooldown.bypass")) {
            int remaining = cooldownManager.getRemainingSeconds(player.getUniqueId());
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("cooldown",
                    "{time}", String.valueOf(remaining)));
            return true;
        }

        Location loc = plugin.getHomeManager().getHome(player.getUniqueId(), homeName);
        if (loc == null) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-not-found",
                    "{home}", homeName));
            return true;
        }

        player.teleport(loc);
        cooldownManager.setCooldown(player.getUniqueId());
        player.sendMessage(plugin.getPrefix() + plugin.getMessage("teleported",
                "{home}", homeName));

        return true;
    }
}
