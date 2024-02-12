package com.sanqiu.rustbuildsystem.util;

import com.sanqiu.rustbuildsystem.RustBuildSystem;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Config {
    private File configFile;
    private YamlConfiguration configYaml;
    private String configName;
    private Plugin plugin;
    public Config(String configName){
        this.configName = configName;
        plugin = RustBuildSystem.getPlugin();
    }
    public void save(){
        try {
            configYaml.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public   YamlConfiguration load() {
        File pluginDataFolder=plugin.getDataFolder();
        File configFile = new File(pluginDataFolder, configName);
        if (!pluginDataFolder.exists())pluginDataFolder.mkdir();
        if (!configFile.exists())plugin.saveResource(configName, true);
        this.configFile = configFile;
        this.configYaml = YamlConfiguration.loadConfiguration(configFile);
        return configYaml;
    }
}