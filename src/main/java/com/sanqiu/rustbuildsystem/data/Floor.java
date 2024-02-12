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
    public boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace) {

        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);
        boolean result1 = true;
        boolean result2 = true;
        boolean result3 = true;
        boolean result4 = true;
        boolean result11 = true;
        boolean result22 = true;
        boolean result33 = true;
        boolean result44 = true;
        for(int i= 0;i<5 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                if(i==0&& j==0|| i==0&&j==4||i==4&&j==0||i==4&&j==4) {
                    if(i==0&&j==0){
                        if(getDownLocatuon(start_loc).getBlock().getType() != outerMaterial) {
                            result11 = false;
                            result33 = false;
                        }
                    }else if(i == 0){
                        if(getDownLocatuon(start_loc).getBlock().getType() != outerMaterial) {
                            result22 = false;
                            result33 = false;
                        }

                    }else if(j == 0){
                        if(getDownLocatuon(start_loc).getBlock().getType() != outerMaterial) {
                            result11 = false;
                            result44 = false;
                        }

                    }else {
                        if(getDownLocatuon(start_loc).getBlock().getType() != outerMaterial) {
                            result22 = false;
                            result44 = false;
                        }
                    }
                }

                if(j == 0){
                    if(getDownLocatuon(start_loc).getBlock().getType() != contentMaterial) result11 = false;
                } else if(j==4){
                    if(getDownLocatuon(start_loc).getBlock().getType() != contentMaterial) result22 = false;
                }else if(i==0){
                    if(getDownLocatuon(start_loc).getBlock().getType() != contentMaterial) result33 = false;
                } else if(i==4){
                    if(getDownLocatuon(start_loc).getBlock().getType() != contentMaterial) result44 = false;
                }

                 if(j == 0){
                    if(getLeftLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result1 = false;

                }
                 if(j==4){
                    if(getRightLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result2= false;
                }
                 if(i==0){
                    if(verticalFace == BlockFace.UP){
                        if(getFrontLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result3= false;
                    } else {
                        if(getBackLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result3= false;
                    }

                }
                 if(i==4){
                    if(verticalFace == BlockFace.UP){
                        if(getBackLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result4= false;
                    }else {
                        if(getFrontLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial) result4= false;
                    }
                }
                if(!(result11 ||result22 ||result33 || result44||
                        result1|| result2|| result3|| result4)) return false;
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            if(verticalFace == BlockFace.UP){
                start_loc = getBackLocatuon(start_loc,blockFace);
            }else {
                start_loc = getFrontLocatuon(start_loc,blockFace);
            }

        }

        return true;
    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace,BlockFace verticalFace){
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
            if(verticalFace == BlockFace.UP){
                start_loc = getBackLocatuon(start_loc,blockFace);
            }else {
                start_loc = getFrontLocatuon(start_loc,blockFace);
            }

        }

        return list;
    }
}
