package com.sanqiu.rustbuildsystem.data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;


public class Wall extends RustBlock {
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace) {

        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<3 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                if(i==0){
                    if(getDownLocatuon(start_loc).getBlock().getType() != outerMaterial) return false;
                }
                if(j!=0&&j!=4){
                    //if(start_loc.getBlock().getType() != Material.AIR) return false;
                }
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            start_loc = getUpLocatuon(start_loc);

        }

        return true;
    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace,BlockFace verticalFace){

        List<RustBlockData> list = new ArrayList<>();

        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<3 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                if(j==0 || j==4){
                    list.add(new RustBlockData(start_loc,outerMaterial));
                }else {
                    list.add(new RustBlockData(start_loc,contentMaterial));
                }
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            start_loc = getUpLocatuon(start_loc);

        }

        return list;
    }
}
