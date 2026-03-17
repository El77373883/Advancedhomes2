package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class AHomeListCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public AHomeListCommand(AdvancedHomesPlugin plugin) {
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

        Map<String, Location> homes = plugin.getHomeManager().getHomes(player.getUniqueId());

        if (homes.isEmpty()) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-homes"));
            return true;
        }

        player.sendMessage(AdvancedHomesPlugin.colorize("&8&m----&r &b&lAdvancedHomes &8&m----"));
        player.sendMessage(AdvancedHomesPlugin.colorize("&7Tienes &e" + homes.size() + " &7home(s):"));
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            Location loc = entry.getValue();
            String world = loc.getWorld() != null ? loc.getWorld().getName() : "?";
            player.sendMessage(AdvancedHomesPlugin.colorize(
                    "  &8» &b" + entry.getKey() + " &7- " + world +
                    " &8(&7" + (int)loc.getX() + "&8, &7" + (int)loc.getY() + "&8, &7" + (int)loc.getZ() + "&8)"
            ));
        }
        player.sendMessage(AdvancedHomesPlugin.colorize("&8&m-----------------------"));

        return true;
    }
}
