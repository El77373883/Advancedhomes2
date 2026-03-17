package com.advancedhomes.managers;

import com.advancedhomes.AdvancedHomesPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final AdvancedHomesPlugin plugin;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public CooldownManager(AdvancedHomesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean isOnCooldown(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return false;
        long elapsed = System.currentTimeMillis() - cooldowns.get(uuid);
        int cooldownMs = getCooldownSeconds() * 1000;
        return elapsed < cooldownMs;
    }

    public int getRemainingSeconds(UUID uuid) {
        if (!cooldowns.containsKey(uuid)) return 0;
        long elapsed = System.currentTimeMillis() - cooldowns.get(uuid);
        int cooldownMs = getCooldownSeconds() * 1000;
        long remaining = cooldownMs - elapsed;
        return (int) Math.ceil(remaining / 1000.0);
    }

    public void setCooldown(UUID uuid) {
        cooldowns.put(uuid, System.currentTimeMillis());
    }

    public void clearCooldown(UUID uuid) {
        cooldowns.remove(uuid);
    }

    private int getCooldownSeconds() {
        return plugin.getConfig().getInt("settings.cooldown", 3);
    }
}
