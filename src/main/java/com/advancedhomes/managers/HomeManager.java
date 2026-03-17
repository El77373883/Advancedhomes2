package com.advancedhomes.managers;

import com.advancedhomes.AdvancedHomesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class HomeManager {

    private final AdvancedHomesPlugin plugin;
    private final File homesFile;
    private FileConfiguration homesConfig;

    // UUID -> (nombre home -> location)
    private final Map<UUID, Map<String, Location>> homesCache = new HashMap<>();

    public HomeManager(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
        this.homesFile = new File(plugin.getDataFolder(), "homes.yml");
        loadAll();
    }

    private void loadAll() {
        if (!homesFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                homesFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("No se pudo crear homes.yml: " + e.getMessage());
            }
        }
        homesConfig = YamlConfiguration.loadConfiguration(homesFile);

        if (homesConfig.getConfigurationSection("homes") == null) return;

        for (String uuidStr : homesConfig.getConfigurationSection("homes").getKeys(false)) {
            UUID uuid;
            try { uuid = UUID.fromString(uuidStr); } catch (Exception e) { continue; }

            Map<String, Location> playerHomes = new HashMap<>();
            var section = homesConfig.getConfigurationSection("homes." + uuidStr);
            if (section == null) continue;

            for (String homeName : section.getKeys(false)) {
                String path = "homes." + uuidStr + "." + homeName + ".";
                String worldName = homesConfig.getString(path + "world");
                World world = Bukkit.getWorld(worldName);
                if (world == null) continue;

                double x = homesConfig.getDouble(path + "x");
                double y = homesConfig.getDouble(path + "y");
                double z = homesConfig.getDouble(path + "z");
                float yaw = (float) homesConfig.getDouble(path + "yaw");
                float pitch = (float) homesConfig.getDouble(path + "pitch");

                playerHomes.put(homeName.toLowerCase(), new Location(world, x, y, z, yaw, pitch));
            }

            homesCache.put(uuid, playerHomes);
        }

        plugin.getLogger().info("Homes cargados correctamente.");
    }

    public void saveAll() {
        for (Map.Entry<UUID, Map<String, Location>> entry : homesCache.entrySet()) {
            String uuidStr = entry.getKey().toString();
            for (Map.Entry<String, Location> homeEntry : entry.getValue().entrySet()) {
                String path = "homes." + uuidStr + "." + homeEntry.getKey() + ".";
                Location loc = homeEntry.getValue();
                homesConfig.set(path + "world", loc.getWorld().getName());
                homesConfig.set(path + "x", loc.getX());
                homesConfig.set(path + "y", loc.getY());
                homesConfig.set(path + "z", loc.getZ());
                homesConfig.set(path + "yaw", loc.getYaw());
                homesConfig.set(path + "pitch", loc.getPitch());
            }
        }

        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar homes.yml: " + e.getMessage());
        }
    }

    private void savePlayer(UUID uuid) {
        Map<String, Location> playerHomes = homesCache.getOrDefault(uuid, new HashMap<>());
        String uuidStr = uuid.toString();

        // Limpiar entradas anteriores
        homesConfig.set("homes." + uuidStr, null);

        for (Map.Entry<String, Location> homeEntry : playerHomes.entrySet()) {
            String path = "homes." + uuidStr + "." + homeEntry.getKey() + ".";
            Location loc = homeEntry.getValue();
            homesConfig.set(path + "world", loc.getWorld().getName());
            homesConfig.set(path + "x", loc.getX());
            homesConfig.set(path + "y", loc.getY());
            homesConfig.set(path + "z", loc.getZ());
            homesConfig.set(path + "yaw", loc.getYaw());
            homesConfig.set(path + "pitch", loc.getPitch());
        }

        try {
            homesConfig.save(homesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Error al guardar home: " + e.getMessage());
        }
    }

    public boolean setHome(UUID uuid, String name, Location location) {
        homesCache.computeIfAbsent(uuid, k -> new HashMap<>()).put(name.toLowerCase(), location);
        savePlayer(uuid);
        return true;
    }

    public boolean deleteHome(UUID uuid, String name) {
        Map<String, Location> playerHomes = homesCache.get(uuid);
        if (playerHomes == null || !playerHomes.containsKey(name.toLowerCase())) return false;
        playerHomes.remove(name.toLowerCase());
        savePlayer(uuid);
        return true;
    }

    public Location getHome(UUID uuid, String name) {
        Map<String, Location> playerHomes = homesCache.get(uuid);
        if (playerHomes == null) return null;
        return playerHomes.get(name.toLowerCase());
    }

    public Map<String, Location> getHomes(UUID uuid) {
        return homesCache.getOrDefault(uuid, new HashMap<>());
    }

    public boolean hasHome(UUID uuid, String name) {
        return getHome(uuid, name) != null;
    }

    public int getHomeCount(UUID uuid) {
        return homesCache.getOrDefault(uuid, new HashMap<>()).size();
    }
}
