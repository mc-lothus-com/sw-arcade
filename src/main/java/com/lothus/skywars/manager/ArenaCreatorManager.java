package com.lothus.skywars.manager;

import com.lothus.skywars.arena.creator.ArenaCreator;

import java.util.HashMap;
import java.util.UUID;

public class ArenaCreatorManager {

    private final HashMap<UUID, ArenaCreator> arenaCreatorHashMap = new HashMap<>();

    public void load(ArenaCreator arenaCreator) {
        arenaCreatorHashMap.put(arenaCreator.getPlayer().getUniqueId(), arenaCreator);
    }

    public void unload(UUID uuid) {
        arenaCreatorHashMap.remove(uuid);
    }

    public ArenaCreator getArenaCreator(UUID uuid) {
        return arenaCreatorHashMap.get(uuid);
    }
}
