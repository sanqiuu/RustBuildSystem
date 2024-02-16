package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Slope extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace, BlockFace verticalFace) {
        Location leftBlockLoc = getLeftLocatuon(location,blockFace).add(0,-1,0);
        Location leftleftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace);
        Location rightBlockLoc = getRightLocatuon(location,blockFace).add(0,-1,0);
        Location rightrightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace);

        return leftleftBlockLoc.getBlock().getType() == outerMaterial &&
                rightrightBlockLoc.getBlock().getType() == outerMaterial ;
    }

    @Override
    public List<RustBlockData> build(Location location, BlockFace blockFace, BlockFace verticalFace) {
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
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }

        return list;
    }
}
