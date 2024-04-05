package com.lothus.skywars.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationUtils {

    public static Location getLocation(String value) {
        World world = Bukkit.getWorld(value.split(",")[0]);
        double x = Double.parseDouble(value.split(",")[1]);
        double y = Double.parseDouble(value.split(",")[2]);
        double z = Double.parseDouble(value.split(",")[3]);
        float yaw = Float.parseFloat(value.split(",")[4]);
        float pitch = Float.parseFloat(value.split(",")[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public static String getData(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
    }
}