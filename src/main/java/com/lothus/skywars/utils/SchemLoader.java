package com.lothus.skywars.utils;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class SchemLoader {

    @Deprecated
    public void loadAndPasteSchematic(File file , Location location) {
        EditSession es = new EditSession(new BukkitWorld(location.getWorld()), Integer.MAX_VALUE);

        try {
            CuboidClipboard cc = CuboidClipboard.loadSchematic(file);
            cc.paste(es, new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void paste(File file, Location location) {
        try {
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1);
            editSession.enableQueue();
            com.sk89q.worldedit.world.World w = editSession.getWorld();
            ClipboardFormat format = ClipboardFormat.findByFile(file);
            assert format != null;
            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read(w.getWorldData());
            Operation operation = new ClipboardHolder(clipboard, w.getWorldData()).createPaste(editSession, w.getWorldData()).to(new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ())).ignoreAirBlocks(false).build();
            Operations.complete(operation);
            editSession.flushQueue();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
