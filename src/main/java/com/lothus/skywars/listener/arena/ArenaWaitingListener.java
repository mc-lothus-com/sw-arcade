package com.lothus.skywars.listener.arena;

import com.lothus.core.api.tag.TagManager;
import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.event.task.ArenaWaitingEvent;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;

public class ArenaWaitingListener implements Listener {
    
    @EventHandler
    public void onWaiting(ArenaWaitingEvent event) {
        Arena arena = event.getArena();

        Bukkit.getOnlinePlayers().forEach(TagManager::update);

        arena.updateGameInfo();

        if (arena.getPlayers().size() >= arena.getMinPlayers() && arena.getPlayers().size() < arena.getMaxPlayers()) {
            arena.getTask().setTime(40);

            arena.setState(GameState.INICIANDO);
            arena.getPlayers().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f40 §asegundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §640 §esegundos!", 20, 20, 20));
            });
            arena.getSpectators().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f40 §asegundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §640 §esegundos!", 20, 20, 20));
            });
        }
        if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
            arena.getTask().setTime(20);

            arena.setState(GameState.INICIANDO);
            arena.getPlayers().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f40 §asegundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §620 §esegundos!", 20, 20, 20));
            });
            arena.getSpectators().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f40 §asegundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §620 §esegundos!", 20, 20, 20));
            });
        }

        arena.getPlayers().forEach(p -> {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(p.getUniqueId());

            if (arenaScoreboard == null) return;

            arenaScoreboard.update();
        });

        arena.getSpectators().forEach(p -> {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(p.getUniqueId());

            if (arenaScoreboard == null) return;

            arenaScoreboard.update();
        });
    }
}
