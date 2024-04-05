package com.lothus.skywars.arena.tasks;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class ArenaTask extends BukkitRunnable {

    @Getter @Setter
    private boolean damage;

    @Getter @Setter
    private long time;

    @Override
    public void run() {
        task();
    }

    public abstract void task();
}
