package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Stair extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace) {
        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);
        for(int i =0 ;i<5;i++){
            Location loc_1 = start_loc;
            for(int j=0 ; j<5 ;j++){
                if(i==0||i==4||j==0||j==4){
                    if(getDownLocatuon(start_loc).getBlock().getType()!=outerMaterial){
                        return false;
                    }
                }
                start_loc = getFrontLocatuon(start_loc,blockFace);
            }
            start_loc = loc_1;
            start_loc = getRightLocatuon(start_loc,blockFace);
        }
        return true;
    }

    @Override
    public List<RustBlockData> build(Location location, BlockFace blockFace) {
        List<RustBlockData> list = new ArrayList<>();


        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);
        for(int i =0 ;i<5;i++){
            Location loc_1 = start_loc;
            for(int j=0 ; j<5 ;j++){
                Location loc_2 = start_loc;
                for(int k=0;k<4;k++){

                    if((i==1 && j==1 && k==0) ||
                            (i==1 && j==2 && k==1)  ||
                            (i==1 && j==3 && k==2)){
                        list.add(new RustBlockData(start_loc,Material.OAK_STAIRS));
                    }
                    else if(((i>=2 &&i<=3) && (j>=1&&j<=3)&& k==3)){
                        list.add(new RustBlockData(start_loc,contentMaterial));
                    }else if (k==3 && (i==0 || i==4 ||j==0 ||j==4)){
                        list.add(new RustBlockData(start_loc,outerMaterial));
                    }


                    start_loc = getUpLocatuon(start_loc);
                }
                start_loc = loc_2;
                start_loc = getFrontLocatuon(start_loc,blockFace);
            }
            start_loc = loc_1;
            start_loc = getRightLocatuon(start_loc,blockFace);
        }

        return list;
    }
}
