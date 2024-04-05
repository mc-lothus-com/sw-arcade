package com.lothus.skywars.platform;

import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.manager.*;
import lombok.Getter;
import lombok.Setter;

public class Platform {

    @Getter @Setter
    private static Arena match;

    @Getter
    private static CageManager cageManager = new CageManager();

    @Getter
    private static ChestManager chestManager = new ChestManager();

    @Getter
    private static ScoreboardManager scoreboardManager = new ScoreboardManager();

    @Getter
    private static GamePlayerManager gamePlayerManager = new GamePlayerManager();


    @Getter
    private static ArenaCreatorManager arenaCreatorManager = new ArenaCreatorManager();
}
