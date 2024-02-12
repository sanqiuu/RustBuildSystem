package com.sanqiu.rustbuildsystem.data;

import org.bukkit.Location;

import java.util.List;

public  class FactionBlockHolder {
    public String holder_uuid;
    public List<Location> locationList;

    public FactionBlockHolder(String holder_uuid, List<Location> locationList) {
        this.holder_uuid = holder_uuid;
        this.locationList = locationList;

    }

}
