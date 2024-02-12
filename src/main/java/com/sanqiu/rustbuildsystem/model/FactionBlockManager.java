package com.sanqiu.rustbuildsystem.model;

import com.sanqiu.rustbuildsystem.data.FactionBlockHolder;
import com.sanqiu.rustbuildsystem.util.Config;
import com.sanqiu.rustbuildsystem.util.JavaUtil;
import org.bukkit.Location;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class FactionBlockManager {
    public static FactionBlockManager INSTANCE = new FactionBlockManager();
    private Config config;
    private YamlConfiguration ymal;
    private FactionBlockManager(){
        config  = new Config("faction.yml");
        ymal = config.load();
        if(!ymal.contains("faction_list")){
            ymal.createSection("faction_list");
        }

    }
    public  List<FactionBlockHolder> faction_list = new ArrayList<>();
    private  FactionBlockHolder getFactionBlockHolder(Player player){
        for(FactionBlockHolder holder:faction_list){
            if(holder.holder_uuid.equals(player.getUniqueId().toString())){
                return holder;
            }
        }
        return null;
    }
    public  boolean placeFactionBlock(Player player,Location location){
        FactionBlockHolder holder = getFactionBlockHolder(player);
        boolean result = true;
        if(holder == null){
            String uuid = player.getUniqueId().toString();
            holder = new FactionBlockHolder(uuid,new ArrayList<>());
            holder.locationList.add(location);
            faction_list.add(holder);
        } else if(holder.locationList.size()<5) {
            holder.locationList.add(location);
        }else {
            result = false;
        }
        if(result){
            save();
        }
        return result;
    }
    public  void breakFactionBlock(Location break_loc){
        List<Location> list = null;
        for(FactionBlockHolder holder:faction_list){
            for (Location location:holder.locationList){
                if(location.equals(break_loc)){
                    list = holder.locationList;
                    break;
                }
            }
        }
        list.remove(break_loc);
        save();
    }
    static  int protect_area_size = 25;
    public  boolean hasPermission(Player player,Location location){
        Location hor_Location = new Location(location.getWorld(),location.getX(),0,location.getZ());
        for (FactionBlockHolder holder:faction_list){
            for(Location loc:holder.locationList){
                Location hor_loc = new Location(loc.getWorld(),loc.getX(),0,loc.getZ());
                if(hor_loc.distance(hor_Location)<=protect_area_size){
                    FactionBlock factionBlock = new FactionBlock(loc);
                    if(!factionBlock.hasPermission(player.getUniqueId())){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public  boolean isProtect(Location location){
        Location hor_Location = new Location(location.getWorld(),location.getX(),0,location.getZ());
        for (FactionBlockHolder holder:faction_list){
            for(Location loc:holder.locationList){
                Location hor_loc = new Location(loc.getWorld(),loc.getX(),0,loc.getZ());
                if(hor_loc.distance(hor_Location)<=protect_area_size){
                    return true;
                }
            }
        }
        return false;
    }
    public   void load(){

        ymal.getConfigurationSection("faction_list").getKeys(false).forEach(key->{
            String holder_uuid = ymal.getString("faction_list."+key+".uuid");
            List<Location> locationList  = JavaUtil.castList(ymal.getList("faction_list."+key+".blocksList"),Location.class);
            FactionBlockHolder holder = new FactionBlockHolder(holder_uuid,locationList);
            faction_list.add(holder);
        });

    }
    public void save(){
        int index = 0;
        for(FactionBlockHolder holder:faction_list){
            String holder_uuid = holder.holder_uuid;
            List<Location> locationList  =  holder.locationList;
            ymal.set("faction_list."+index+".uuid",holder_uuid);
            ymal.set("faction_list."+index+".blocksList",locationList);
            index++;
        }
        config.save();
    }
}