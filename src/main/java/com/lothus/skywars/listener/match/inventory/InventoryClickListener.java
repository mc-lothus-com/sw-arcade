package com.lothus.skywars.listener.match.inventory;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        Arena a = Platform.getMatch();

        if (a == null)return;

        if (a.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
            return;
        }

        if (a.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }
    }
}
