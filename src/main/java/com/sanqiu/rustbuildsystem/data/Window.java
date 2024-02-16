package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class Window extends RustBlock{
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace,BlockFace verticalFace) {
        Location leftBlockLoc = getLeftLocatuon(location,blockFace).add(0,-1,0);
        Location leftleftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace);
        Location rightBlockLoc = getRightLocatuon(location,blockFace).add(0,-1,0);
        Location rightrightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace);

        return leftBlockLoc.getBlock().getType() == outerMaterial &&
                rightrightBlockLoc.getBlock().getType() == outerMaterial ;
    }

    @Override
    public List<RustBlockData> build(Location location, BlockFace blockFace,BlockFace verticalFace) {

        List<RustBlockData> list = new ArrayList<>();
        list.add(new RustBlockData(location, contentMaterial));
        list.add(new RustBlockData(location.clone().add(0,2,0),contentMaterial));

        Location leftBlockLoc = getLeftLocatuon(location,blockFace);
        list.add(new RustBlockData(leftBlockLoc,contentMaterial));
        list.add(new RustBlockData(leftBlockLoc.clone().add(0,2,0),contentMaterial));

        leftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace);
        list.add(new RustBlockData(leftBlockLoc,outerMaterial));
        list.add(new RustBlockData(leftBlockLoc.clone().add(0,1,0),outerMaterial));
        list.add(new RustBlockData(leftBlockLoc.clone().add(0,2,0),outerMaterial));

        Location rightBlockLoc = getRightLocatuon(location,blockFace);
        list.add(new RustBlockData(rightBlockLoc,contentMaterial));
        list.add(new RustBlockData(rightBlockLoc.clone().add(0,2,0),contentMaterial));

        rightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace);
        list.add(new RustBlockData(rightBlockLoc,outerMaterial));
        list.add(new RustBlockData(rightBlockLoc.clone().add(0,1,0),outerMaterial));
        list.add(new RustBlockData(rightBlockLoc.clone().add(0,2,0),outerMaterial));
        return list;
    }
}
