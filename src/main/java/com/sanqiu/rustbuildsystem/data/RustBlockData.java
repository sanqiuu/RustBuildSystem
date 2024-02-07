package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class RustBlockData {
    public Location location;
    public Material material;
    public RustBlockData(Location location, Material material){
        this.location = location;
        this.material = material;
    }

}
