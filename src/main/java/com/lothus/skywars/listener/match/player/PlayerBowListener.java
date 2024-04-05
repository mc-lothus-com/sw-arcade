package com.lothus.skywars.listener.match.player;

import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.platform.Platform;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerBowListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;


        if (!(event.getDamager() instanceof Arrow)) return;

        Arena arena = Platform.getMatch();
        Arrow arrow = (Arrow) event.getDamager();
        ProjectileSource shooter = arrow.getShooter();
        if (!(shooter instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player playerShooter = (Player) shooter;

        ArenaTeam team = arena.getTeam(player);

        if (arena.getTeam(playerShooter) == team) {
            event.setCancelled(true);
            return;
        }

        LothPlayer l = Core.getPlayerController().get(player.getUniqueId());

        String name = (l.getSocial().getFake().getName() != l.getName() ? l.getSocial().getFake().getRank().getColor() + l.getSocial().getFake().getName() : l.getGroup().getTag().getColor() + l.getName());
        playerShooter.sendMessage("§7" + name + " §eestá com §c" + String.format("%.1f", player.getHealth()) + " §ede HP.");
    }
}
