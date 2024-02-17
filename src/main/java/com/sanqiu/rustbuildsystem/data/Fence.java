package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Fence extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    Material IronString = Material.COBWEB;
    Material FenceMaterial = Material.OAK_FENCE;
    private int length = 7;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace, BlockFace verticalFace) {

        Location start_loc = getLeftLocatuon(getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace),blockFace);
        boolean isFirstLay = true;
        boolean isEndLay = false;
        for(int i=0;;i++){
            Location loc = start_loc;

            if(!isLineAir(getDownLocatuon(start_loc),blockFace)){
                isEndLay = true;
            }
            for(int j =0;j<length ;j++){
                if(isFirstLay){
                    if(start_loc.getBlock().getType().isSolid()) return false;
                }

                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            isFirstLay = false;
            start_loc = loc;
            if(isEndLay){
                if(isLineBuilding(getDownLocatuon(start_loc),blockFace)) return false;
                return i>=2;
            }
            start_loc = getDownLocatuon(start_loc);

        }
    }
    private boolean isLineBuilding(Location location, BlockFace blockFace){
        Location loc = location.clone();
        for(int i = 0;i<length;i++){
            Material material = loc.getBlock().getType();
            if(material==Material.OAK_LOG ||material == IronString){
                return true;
            }
            loc = getRightLocatuon(loc,blockFace);
        }
        return false;
    }
    private boolean isLineAir(Location location, BlockFace blockFace){
        Location loc = location.clone();
        for(int i = 0;i<length;i++){
            if(loc.getBlock().getType().isSolid()){
                return false;
            }
            loc = getRightLocatuon(loc,blockFace);
        }
        return true;
    }
    @Override
    public List<RustBlockData> build(Location location, BlockFace blockFace, BlockFace verticalFace) {
        List<RustBlockData> list = new ArrayList<>();

        Location start_loc = getLeftLocatuon(getLeftLocatuon(getLeftLocatuon(location,blockFace),blockFace),blockFace);
        boolean isFirstLay = true;
        boolean isEndLay = false;
        for(;;){
            Location loc = start_loc;

            if(!isLineAir(getDownLocatuon(start_loc),blockFace)){
                isEndLay = true;
            }
            for(int j =0;j<length ;j++){
                if(isFirstLay){
                    list.add(new RustBlockData(start_loc,IronString));
                }else {
                    if(j==0 || j==length-1){
                        list.add(new RustBlockData(start_loc,outerMaterial));
                    }else {
                        if(isEndLay){
                            list.add(new RustBlockData(start_loc,contentMaterial));

                        }else {
                            list.add(new RustBlockData(start_loc,FenceMaterial));
                        }
                    }
                }
                start_loc = getRightLocatuon(start_loc,blockFace);
            }
            isFirstLay = false;
            if(isEndLay){
                return list;
            }
            start_loc = loc;
            start_loc = getDownLocatuon(start_loc);

        }

    }
}
