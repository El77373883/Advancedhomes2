package com.advancedhomes;

import com.advancedhomes.commands.*;
import com.advancedhomes.listeners.GUIListener;
import com.advancedhomes.managers.HomeManager;
import com.advancedhomes.managers.CooldownManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancedHomesPlugin extends JavaPlugin {

    private static AdvancedHomesPlugin instance;
    private HomeManager homeManager;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        homeManager = new HomeManager(this);
        cooldownManager = new CooldownManager(this);

        // Registrar comandos
        getCommand("ahome").setExecutor(new AHomeCommand(this));
        getCommand("asethome").setExecutor(new ASetHomeCommand(this));
        getCommand("adeletehome").setExecutor(new ADeleteHomeCommand(this));
        getCommand("ahomelist").setExecutor(new AHomeListCommand(this));
        getCommand("ahomes").setExecutor(new AHomesGUICommand(this));
        getCommand("ahometp").setExecutor(new AHomeTpCommand(this));
        getCommand("ahomereload").setExecutor(new AHomeReloadCommand(this));

        // Registrar listeners
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);

        getLogger().info("AdvancedHomes Premium v1.0.0 activado correctamente.");
        getLogger().info("Desarrollado con amor para Spigot 1.21");
    }

    @Override
    public void onDisable() {
        if (homeManager != null) {
            homeManager.saveAll();
        }
        getLogger().info("AdvancedHomes Premium desactivado.");
    }

    public static AdvancedHomesPlugin getInstance() {
        return instance;
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public String getPrefix() {
        return colorize(getConfig().getString("messages.prefix", "&8[&bAdvancedHomes&8] "));
    }

    public String getMessage(String key) {
        return colorize(getConfig().getString("messages." + key, "&cMensaje no encontrado: " + key));
    }

    public String getMessage(String key, String... replacements) {
        String msg = getMessage(key);
        for (int i = 0; i < replacements.length - 1; i += 2) {
            msg = msg.replace(replacements[i], replacements[i + 1]);
        }
        return msg;
    }

    public static String colorize(String text) {
        return text.replace("&", "\u00a7");
    }
}
