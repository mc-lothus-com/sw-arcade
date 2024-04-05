package com.lothus.skywars.arena.cube;

import com.lothus.core.Core;
import com.lothus.skywars.Instance;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ArenaCube {

    private World world;
    private int minX, maxX;
    private int minY, maxY;
    private int minZ, maxZ;

    public ArenaCube(Location loc1, Location loc2) {
        world = loc1.getWorld();

        minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
    }

    public List<Block> getBlocks() {
        List<Block> blocks = new ArrayList<>();

        for(int x = this.minX; x < this.maxX; ++x) {
            for(int y = this.minY; y < this.maxY; ++y) {
                for(int z = this.minZ; z < this.maxZ; ++z) {
                    blocks.add(this.world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }


    public void save(String world, String folder) {
        World w = Bukkit.getWorld(world);
        w.save();

        try {
            File dir = new File(Instance.getInstance().getDataFolder().getPath() + "/"+folder+"/");
            File to = new File("/home/container/" + world);

            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.copyDirectory(to, dir);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paste(String world, String folder) {
        try {
            if (!Bukkit.unloadWorld(world, false)) {
                Core.getLogger().info("Não foi possivel descarregar o mundo " + world);
                return;
            }

            File dir = new File(Instance.getInstance().getDataFolder().getPath() + "/"+folder+"/");
            File to = new File("/home/container/" + world);

            FileUtils.deleteDirectory(to);

            if (!to.exists()) {
                to.mkdirs();
            }
            FileUtils.copyDirectory(dir, to);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Location getMinPoint(){
        return new Location(world, minX, minY, minZ);
    }

    public Location getMaxPoint(){
        return new Location(world, maxX, maxY, maxZ);
    }

}
