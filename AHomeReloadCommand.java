package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AHomeReloadCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public AHomeReloadCommand(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("advancedhomes.admin")) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("no-permission"));
            return true;
        }

        plugin.reloadConfig();
        sender.sendMessage(plugin.getPrefix() + plugin.getMessage("reload"));
        return true;
    }
}
