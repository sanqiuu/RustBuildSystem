package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class BaseStair extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace) {

        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);
        boolean topInterface = true;
        for(int i= 0;i<6 ;i++){
            Location loc = start_loc;
            topInterface = true;
            for(int j=0;j<5;j++){
                if (getFrontLocatuon(start_loc,blockFace).getBlock().getType() != outerMaterial){
                    topInterface = false;
                    break;
                }
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            if(topInterface){
                break;
            }
            start_loc = loc;
            start_loc = getUpLocatuon(start_loc);
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }




        Location leftBlockLoc = getLeftLocatuon(location,blockFace).add(0,-1,0);
        Location leftleftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace).add(0,-1,0);
        Location rightBlockLoc = getRightLocatuon(location,blockFace).add(0,-1,0);
        Location rightrightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace).add(0,-1,0);

        return (leftBlockLoc.getBlock().getType() != Material.AIR &&
                leftleftBlockLoc.getBlock().getType() != Material.AIR &&
                rightBlockLoc.getBlock().getType()  != Material.AIR &&
                rightrightBlockLoc.getBlock().getType() != Material.AIR  &&
                location.add(0,-1,0).getBlock().getType() != Material.AIR) && topInterface ;
    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace,BlockFace verticalFace){
        List<RustBlockData> list = new ArrayList<>();


        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<6 ;i++){
            Location loc = start_loc;
            Location start_loc_clone = start_loc;
            boolean needStop = true;
            for(int j=0;j<5;j++){
                if (getFrontLocatuon(start_loc_clone,blockFace).getBlock().getType() != outerMaterial){
                    needStop = false;
                    break;
                }
                start_loc_clone = getRightLocatuon(start_loc_clone,blockFace);
            }

            for(int j =0;j<5 ;j++){
                list.add(new RustBlockData(start_loc,contentMaterial));

                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            if(needStop) return list;
            start_loc = loc;
            start_loc = getUpLocatuon(start_loc);
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }

        return list;
    }
}