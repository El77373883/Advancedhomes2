package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import com.advancedhomes.gui.HomeGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHomesGUICommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public AHomesGUICommand(AdvancedHomesPlugin plugin) {
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

        if (plugin.getHomeManager().getHomes(player.getUniqueId()).isEmpty()) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-homes"));
            return true;
        }

        new HomeGUI(plugin).openGUI(player);
        return true;
    }
}
