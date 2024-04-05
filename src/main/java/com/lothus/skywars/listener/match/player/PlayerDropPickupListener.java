package com.lothus.skywars.listener.match.player;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class PlayerDropPickupListener implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null)return;

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isSpectator(player) && !arena.isPlayer(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null)return;

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isSpectator(player) && !arena.isPlayer(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
        }
    }
}
