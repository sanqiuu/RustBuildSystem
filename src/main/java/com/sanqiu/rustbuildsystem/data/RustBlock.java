package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public abstract class RustBlock {

    protected Location getLeftLocatuon(Location location, BlockFace blockFace){
        Block block = location.getBlock();
        Block result = null;
        switch (blockFace){
            case EAST:
                result = block.getRelative(BlockFace.NORTH);
                break;
            case WEST:
                result = block.getRelative(BlockFace.SOUTH);
                break;
            case SOUTH:
                result = block.getRelative(BlockFace.EAST);
                break;
            case NORTH:
                result = block.getRelative(BlockFace.WEST);
                break;
        }
        return result.getLocation();
    }
    protected Location getRightLocatuon(Location location, BlockFace blockFace){
        Block block = location.getBlock();
        Block result = null;
        switch (blockFace){
            case EAST:
                result = block.getRelative(BlockFace.SOUTH);
                break;
            case WEST:
                result = block.getRelative(BlockFace.NORTH);
                break;
            case SOUTH:
                result = block.getRelative(BlockFace.WEST);
                break;
            case NORTH:
                result = block.getRelative(BlockFace.EAST);
                break;
        }
        return result.getLocation();
    }
    protected Location getFrontLocatuon(Location location, BlockFace blockFace){
        Block block = location.getBlock();
        Block result = block.getRelative(blockFace);

        return result.getLocation();
    }
    protected Location getBackLocatuon(Location location, BlockFace blockFace){
        Block block = location.getBlock();
        Block result = block.getRelative(blockFace.getOppositeFace());

        return result.getLocation();
    }
    protected Location getUpLocatuon(Location location){

        return location.clone().add(0,1,0);
    }
    protected Location getDownLocatuon(Location location){

        return location.clone().add(0,-1,0);
    }
    public abstract boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace);

    public abstract List<RustBlockData> build(Location location, BlockFace blockFace,BlockFace verticalFace);
}
