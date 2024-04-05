package com.lothus.skywars.manager;

import com.lothus.skywars.player.GamePlayer;

import java.util.HashMap;
import java.util.UUID;

public class GamePlayerManager {

    private final HashMap<UUID, GamePlayer> players = new HashMap<>();

    public void load(GamePlayer gamePlayer) {
        players.put(gamePlayer.getUniqueId(), gamePlayer);
    }

    public void unload(UUID uniqueId) {
        players.remove(uniqueId);
    }

    public GamePlayer get(UUID uuid) {
        return players.get(uuid);
    }

}
