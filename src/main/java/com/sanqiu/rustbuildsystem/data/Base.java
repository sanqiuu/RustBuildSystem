package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Base extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace) {


        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<5 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                int lay_num = 0;
                if(i==0&&j==0 || i==0&&j==4 ||i==4&&j==0 ||i==4&&j==4){
                    Location bottom = getDownLocatuon(start_loc);
                    do{
                        Material material = bottom.getBlock().getType();
                        if(!material.isSolid()){
                            lay_num++;
                            bottom = getDownLocatuon(bottom);
                        }else {
                            break;
                        }
                    }while (true);
                }
                if(lay_num>=6){
                    //return false;
                }
                if(start_loc.getBlock().getType().isSolid() && start_loc.getBlock().getType()!=outerMaterial){
                    return false;
                }

                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            start_loc = loc;
            start_loc = getFrontLocatuon(start_loc,blockFace);
        }
    return  true;

    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace,BlockFace verticalFace){
        List<RustBlockData> list = new ArrayList<>();


        Location start_loc = getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace);

        for(int i= 0;i<5 ;i++){
            Location loc = start_loc;
            for(int j =0;j<5 ;j++){
                if(i==0&&j==0 || i==0&&j==4 ||i==4&&j==0 ||i==4&&j==4){
                    Location bottom = getDownLocatuon(start_loc);
                    do{
                        if(!bottom.getBlock().getType().isSolid()){
                            list.add(new RustBlockData(bottom,outerMaterial));
                            bottom = getDownLocatuon(bottom);
                        }else {
                            break;
                        }
                    }while (true);
                }
                if(j == 0 || j==4 || i==0 || i==4){
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
