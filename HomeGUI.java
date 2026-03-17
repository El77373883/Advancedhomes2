package com.advancedhomes.gui;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class HomeGUI {

    private final AdvancedHomesPlugin plugin;

    public HomeGUI(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Map<String, Location> homes = plugin.getHomeManager().getHomes(player.getUniqueId());

        int size = Math.max(9, ((homes.size() / 9) + 1) * 9);
        if (size > 54) size = 54;

        String title = AdvancedHomesPlugin.colorize(
                plugin.getConfig().getString("messages.gui-title", "&8&lMis Homes")
        );

        Inventory inv = Bukkit.createInventory(null, size, title);

        // Bordes decorativos
        fillBorders(inv, size);

        // Slots disponibles (sin borde)
        int slot = 10;
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            if (slot >= size - 9) break;

            String homeName = entry.getKey();
            Location loc = entry.getValue();

            ItemStack item = new ItemStack(Material.BED);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(AdvancedHomesPlugin.colorize("&b&l" + capitalize(homeName)));

            String worldName = loc.getWorld() != null ? loc.getWorld().getName() : "desconocido";
            String coords = String.format("&7X: &f%.1f &7Y: &f%.1f &7Z: &f%.1f", loc.getX(), loc.getY(), loc.getZ());

            String teleportLore = plugin.getConfig().getString("messages.gui-teleport-lore", "&aClick izquierdo para teletransportarte");
            String deleteLore = plugin.getConfig().getString("messages.gui-delete-lore", "&cClick derecho para eliminar");

            meta.setLore(Arrays.asList(
                    AdvancedHomesPlugin.colorize("&7Mundo: &f" + worldName),
                    AdvancedHomesPlugin.colorize(coords),
                    "",
                    AdvancedHomesPlugin.colorize(teleportLore),
                    AdvancedHomesPlugin.colorize(deleteLore)
            ));

            item.setItemMeta(meta);
            inv.setItem(slot, item);

            slot++;
            // Saltar slots de borde
            if ((slot + 1) % 9 == 0) slot += 2;
        }

        // Boton de cerrar
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(AdvancedHomesPlugin.colorize("&c&lCerrar"));
        closeItem.setItemMeta(closeMeta);
        inv.setItem(size - 5, closeItem);

        player.openInventory(inv);
    }

    private void fillBorders(Inventory inv, int size) {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);

        // Fila superior e inferior
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, glass);
            inv.setItem(size - 9 + i, glass);
        }

        // Lados
        for (int i = 9; i < size - 9; i += 9) {
            inv.setItem(i, glass);
            inv.setItem(i + 8, glass);
        }
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
