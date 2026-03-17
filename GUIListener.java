package com.advancedhomes.listeners;

import com.advancedhomes.AdvancedHomesPlugin;
import com.advancedhomes.managers.CooldownManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class GUIListener implements Listener {

    private final AdvancedHomesPlugin plugin;

    public GUIListener(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String guiTitle = AdvancedHomesPlugin.colorize(
                plugin.getConfig().getString("messages.gui-title", "&8&lMis Homes")
        );

        if (!event.getView().getTitle().equals(guiTitle)) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        // Boton cerrar
        if (clicked.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }

        // Paneles decorativos
        if (clicked.getType() == Material.BLACK_STAINED_GLASS_PANE) return;

        // Home item
        if (clicked.getType() == Material.BED) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null || meta.getDisplayName() == null) return;

            // Extraer nombre limpio
            String displayName = meta.getDisplayName()
                    .replace("\u00a7b", "").replace("\u00a7l", "").replace("\u00a7", "").trim();
            String homeName = displayName.toLowerCase();

            if (event.getClick() == ClickType.LEFT) {
                // Teletransportar
                CooldownManager cooldownManager = plugin.getCooldownManager();

                if (cooldownManager.isOnCooldown(player.getUniqueId())
                        && !player.hasPermission("advancedhomes.cooldown.bypass")) {
                    int remaining = cooldownManager.getRemainingSeconds(player.getUniqueId());
                    player.sendMessage(plugin.getPrefix() + plugin.getMessage("cooldown",
                            "{time}", String.valueOf(remaining)));
                    return;
                }

                Location loc = plugin.getHomeManager().getHome(player.getUniqueId(), homeName);
                if (loc == null) {
                    player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-not-found",
                            "{home}", homeName));
                    player.closeInventory();
                    return;
                }

                player.closeInventory();
                player.teleport(loc);
                cooldownManager.setCooldown(player.getUniqueId());
                player.sendMessage(plugin.getPrefix() + plugin.getMessage("teleported",
                        "{home}", homeName));

            } else if (event.getClick() == ClickType.RIGHT) {
                // Eliminar home
                boolean deleted = plugin.getHomeManager().deleteHome(player.getUniqueId(), homeName);
                if (deleted) {
                    player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-deleted",
                            "{home}", homeName));
                } else {
                    player.sendMessage(plugin.getPrefix() + plugin.getMessage("home-not-found",
                            "{home}", homeName));
                }
                player.closeInventory();
                // Reabrir GUI actualizado
                plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                    new com.advancedhomes.gui.HomeGUI(plugin).openGUI(player);
                }, 2L);
            }
        }
    }
}
