package com.sanqiu.rustbuildsystem;


import com.jeff_media.customblockdata.CustomBlockData;
import com.sanqiu.rustbuildsystem.command.MainCommand;
import com.sanqiu.rustbuildsystem.listener.BuildingMapListener;
import com.sanqiu.rustbuildsystem.listener.CodeDoorListener;
import com.sanqiu.rustbuildsystem.listener.FactionBlockListener;
import com.sanqiu.rustbuildsystem.model.FactionBlockManager;
import com.sanqiu.rustbuildsystem.runnable.FactionBlockChecker;
import org.bukkit.plugin.java.JavaPlugin;

public final class RustBuildSystem extends JavaPlugin {
    private static RustBuildSystem plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        setPlugin(this);
        FactionBlockManager.INSTANCE.load();
        getServer().getPluginCommand("rustbuild").setExecutor(new MainCommand());
        CustomBlockData.registerListener(this);
        getServer().getPluginManager().registerEvents(new BuildingMapListener(), this);
        getServer().getPluginManager().registerEvents(new CodeDoorListener(), this);
        getServer().getPluginManager().registerEvents(new FactionBlockListener(), this);
        new FactionBlockChecker().runTaskTimer(this, 0,5*60*20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        FactionBlockManager.INSTANCE.save();
    }
    public static RustBuildSystem getPlugin() {
        return plugin;
    }

    public static void setPlugin(RustBuildSystem plugin) {
        RustBuildSystem.plugin = plugin;
    }
}
