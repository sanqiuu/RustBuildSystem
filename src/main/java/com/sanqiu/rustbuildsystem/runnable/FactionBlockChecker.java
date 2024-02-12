package com.sanqiu.rustbuildsystem.runnable;

import com.sanqiu.rustbuildsystem.data.FactionBlockHolder;
import com.sanqiu.rustbuildsystem.model.FactionBlockManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;

public class FactionBlockChecker extends BukkitRunnable {

    @Override
    public void run() {
        for(FactionBlockHolder holder : FactionBlockManager.INSTANCE.faction_list){

            holder.locationList.removeIf(location -> location.getBlock().getType() != Material.BEACON);
        }
    }
}
