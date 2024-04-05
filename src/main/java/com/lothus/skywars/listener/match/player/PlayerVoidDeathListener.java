package com.lothus.skywars.listener.match.player;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerVoidDeathListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        Arena arena = Platform.getMatch();

        if (player.isDead())return;
        if (player.getHealth() == 0)return;

        if (arena ==null)return;

        if (arena.getState() != GameState.EM_JOGO)return;


        if (location.getY() <= -20) {
            if (arena.isSpectator(player)) {
                player.teleport(arena.getLobby());
                return;
            }

            player.setHealth(0);
        }
    }
}
