package com.lothus.skywars.manager;

import com.lothus.skywars.scoreboard.ArenaScoreboard;

import java.util.HashMap;
import java.util.UUID;

public class ScoreboardManager {

    private final HashMap<UUID, ArenaScoreboard> scoreboards = new HashMap<>();

    public void load(UUID uniqueId, ArenaScoreboard scoreboard) {
        scoreboards.put(uniqueId, scoreboard);
    }

    public void unload(UUID uniqueId) {
        scoreboards.remove(uniqueId);
    }

    public ArenaScoreboard getScoreboard(UUID uniqueId) {
        return scoreboards.get(uniqueId);
    }
}
