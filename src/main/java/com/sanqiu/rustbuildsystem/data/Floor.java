package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Floor extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace) {

        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);
        start_loc = getBackLocatuon(start_loc,blockFace);
        boolean p1= true;
        boolean p2= true;
        boolean p3= true;
        boolean p4= true;
        for(int i= 0;i<7 ;i++){

            Location loc = start_loc;
            for(int j =0;j<7 ;j++){

                if(i==0&& j==0|| i==0&&j==6||i==6&&j==0||i==6&&j==6) continue;
                if(i==0){
                    if(start_loc.getBlock().getType()!= outerMaterial) p1 = false;
                }else if(i==6){
                    if(start_loc.getBlock().getType()!= outerMaterial) p2 = false;
                }else if(j==0){
                    if(start_loc.getBlock().getType()!= outerMaterial) p3 = false;
                }else if(j==6){
                    if(start_loc.getBlock().getType()!= outerMaterial) p4 = false;
                }
                if(!(p1 || p2||p3||p4)){
                    return false;
                }
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }
        return true;
    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace){
        List<RustBlockData> list = new ArrayList<>();



        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<5 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                if(i==0&& j==0|| i==0&&j==4||i==4&&j==0||i==4&&j==4){
                    list.add(new RustBlockData(start_loc,outerMaterial));
                }
                 else if(j == 0 || j==4 || i==0 || i==4){
                     list.add(new RustBlockData(start_loc,outerMaterial));
                 }else {
                     list.add(new RustBlockData(start_loc,contentMaterial));
                 }

                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }

        return list;
    }
}
