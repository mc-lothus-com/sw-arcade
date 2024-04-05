package com.lothus.skywars.event.state;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ArenaStateChangeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    boolean cancel = false;

    Arena arena;
    GameState state;

    public ArenaStateChangeEvent(Arena arena, GameState state) {
        this.arena = arena;
        this.state = state;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean b) {
        cancel = b;
    }
}
