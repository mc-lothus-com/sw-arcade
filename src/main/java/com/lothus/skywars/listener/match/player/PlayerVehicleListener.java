package com.lothus.skywars.listener.match.player;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.platform.Platform;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PlayerVehicleListener implements Listener {

    @EventHandler
    public void onLeaveVehicle(VehicleExitEvent event) {
        if (Platform.getMatch().getState() != GameState.ENCERRANDO)return;

        event.setCancelled(true);
    }
}
