package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.List;

public class HalfWall extends RustBlock {
    Material contentMaterial = Material.OAK_PLANKS;
    Material outerMaterial = Material.OAK_LOG;
    @Override
    public boolean canBuild(Location location, BlockFace blockFace) {
        Location leftBlockLoc = getLeftLocatuon(location,blockFace).add(0,-1,0);
        Location leftleftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace);
        Location rightBlockLoc = getRightLocatuon(location,blockFace).add(0,-1,0);
        Location rightrightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace);

        return leftBlockLoc.getBlock().getType() == outerMaterial &&
                leftleftBlockLoc.getBlock().getType() == outerMaterial &&
                rightBlockLoc.getBlock().getType() == outerMaterial &&
                rightrightBlockLoc.getBlock().getType() == outerMaterial &&
                location.add(0,-1,0).getBlock().getType() == outerMaterial;
    }
    @Override
    public List<RustBlockData> build(Location location , BlockFace blockFace){

        List<RustBlockData> list = new ArrayList<>();
        list.add(new RustBlockData(location,contentMaterial));

        Location leftBlockLoc = getLeftLocatuon(location,blockFace);
        list.add(new RustBlockData(leftBlockLoc,contentMaterial));

        Location leftleftBlockLoc = getLeftLocatuon(leftBlockLoc,blockFace);
        list.add(new RustBlockData(leftleftBlockLoc,outerMaterial));

        Location rightBlockLoc = getRightLocatuon(location,blockFace);
        list.add(new RustBlockData(rightBlockLoc,contentMaterial));

        Location rightrightBlockLoc = getRightLocatuon(rightBlockLoc,blockFace);
        list.add(new RustBlockData(rightrightBlockLoc,outerMaterial));
        return list;
    }
}
