package com.lothus.skywars.listener.match.player;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerFoodListener implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player))return;

        Player player = (Player) event.getEntity();

        Arena arena = Platform.getMatch();

        if (arena == null) {
            event.setCancelled(true);
            return;
        }

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isPlayer(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
        }
    }

}
