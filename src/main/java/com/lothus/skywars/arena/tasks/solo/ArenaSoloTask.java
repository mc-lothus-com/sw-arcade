package com.lothus.skywars.arena.tasks.solo;

import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.tasks.ArenaTask;
import com.lothus.skywars.event.task.*;
import org.bukkit.*;

public class ArenaSoloTask extends ArenaTask {

    private Arena arena;

    public ArenaSoloTask(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void task() {
        switch (arena.getState()) {
            case ESPERANDO:
                Bukkit.getPluginManager().callEvent(new ArenaWaitingEvent(arena));
                break;
            case INICIANDO:
                setTime(getTime()-1);
                Bukkit.getPluginManager().callEvent(new ArenaStartingEvent(arena));
                break;
            case PREPARANDO:
                setTime(getTime()-1);
                Bukkit.getPluginManager().callEvent(new ArenaPreparingEvent(arena));
                break;
            case EM_JOGO:
                setTime(getTime()-1);
                Bukkit.getPluginManager().callEvent(new ArenaPlayingEvent(arena));
                break;
            case ENCERRANDO:
                setTime(getTime()-1);
                Bukkit.getPluginManager().callEvent(new ArenaTurningOffEvent(arena));
                break;
            case REINICIANDO:
                setTime(getTime()-1);
                Bukkit.shutdown();
                break;
        }
    }
}
