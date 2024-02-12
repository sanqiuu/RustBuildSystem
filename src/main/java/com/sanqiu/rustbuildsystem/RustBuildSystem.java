package com.sanqiu.rustbuildsystem;


import com.jeff_media.customblockdata.CustomBlockData;
import com.sanqiu.rustbuildsystem.command.MainCommand;
import com.sanqiu.rustbuildsystem.listener.BuildingMapListener;
import com.sanqiu.rustbuildsystem.listener.CodeDoorListener;
import com.sanqiu.rustbuildsystem.listener.FactionBlockListener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

public final class RustBuildSystem extends JavaPlugin {
    private static RustBuildSystem plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        setPlugin(this);
        getServer().getPluginCommand("rustbuild").setExecutor(new MainCommand());
        CustomBlockData.registerListener(this);
        getServer().getPluginManager().registerEvents(new BuildingMapListener(), this);
        getServer().getPluginManager().registerEvents(new CodeDoorListener(), this);
        getServer().getPluginManager().registerEvents(new FactionBlockListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static RustBuildSystem getPlugin() {
        return plugin;
    }

    public static void setPlugin(RustBuildSystem plugin) {
        RustBuildSystem.plugin = plugin;
    }
}
