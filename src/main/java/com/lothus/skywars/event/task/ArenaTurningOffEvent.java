package com.lothus.skywars.event.task;

import com.lothus.skywars.arena.Arena;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ArenaTurningOffEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    boolean cancel = false;

    Arena arena;

    public ArenaTurningOffEvent(Arena arena) {
        this.arena = arena;
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

