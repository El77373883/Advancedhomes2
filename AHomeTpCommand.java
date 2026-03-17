package com.advancedhomes.commands;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AHomeTpCommand implements CommandExecutor {

    private final AdvancedHomesPlugin plugin;

    public AHomeTpCommand(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Solo jugadores pueden usar este comando.");
            return true;
        }

        if (!player.hasPermission("advancedhomes.tp.others")) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("no-permission"));
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getPrefix() + AdvancedHomesPlugin.colorize(
                    "&cUso: /ahometp <jugador> <home>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(plugin.getPrefix() + plugin.getMessage("player-not-found",
                    "{player}", args[0]));
            return true;
        }

        String homeName = args[1].toLowerCase();
        Location loc = plugin.getHomeManager().getHome(target.getUniqueId(), homeName);

        if (loc == null) {
            player.sendMessage(plugin.getPrefix() + AdvancedHomesPlugin.colorize(
                    "&cEl jugador &e" + target.getName() + " &cno tiene un home llamado &e" + homeName));
            return true;
        }

        player.teleport(loc);
        player.sendMessage(plugin.getPrefix() + AdvancedHomesPlugin.colorize(
                "&aTeletransportado al home &e" + homeName + " &ade &e" + target.getName()));

        return true;
    }
}
