package com.lothus.skywars.listener.match.block;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BreakPlaceListener implements Listener {
    
    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Arena arena = Platform.getMatch();

        if (arena == null)return;

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isPlayer(player) && !arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Arena arena = Platform.getMatch();

        if (arena == null)return;

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isPlayer(player) && !arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
            return;
        }
    }
}
