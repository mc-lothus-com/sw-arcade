package com.lothus.skywars.listener.match.player;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        Player player = ((Player) event.getEntity()).getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null) {
            event.setCancelled(true);
            return;
        }

        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
            return;
        }

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (!arena.getTask().isDamage()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Arena arena = Platform.getMatch();

        if (event.getEntity() instanceof EnderDragon) {
            if (arena.getState() != GameState.EM_JOGO) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getDamager() instanceof Player) {
            if (arena.isSpectator((Player) event.getDamager())) {
                event.setCancelled(true);
                return;
            }
            if (event.getEntity() instanceof Player && arena.isSpectator((Player) event.getEntity())) {
                event.setCancelled(true);
                return;
            }
        }

        if (!(event.getEntity() instanceof Player)) return;


        Player player = ((Player) event.getEntity()).getPlayer();
        ArenaTeam team = arena.getTeam(player);

        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (!(projectile.getShooter() instanceof Player))return;
            Player playerShooter = (Player) projectile.getShooter();

            if (arena.getTeam(playerShooter) == team) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getDamager() instanceof EnderPearl) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getDamager() instanceof Player)) return;

        Player damager = ((Player) event.getDamager()).getPlayer();


        if (arena.getState() != GameState.EM_JOGO) {
            event.setCancelled(true);
            return;
        }

        if (arena.isSpectator(damager)) {
            event.setCancelled(true);
            return;
        }

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        if (arena.getTeam(damager) == team) {
            event.setCancelled(true);
            return;
        }
    }

}
