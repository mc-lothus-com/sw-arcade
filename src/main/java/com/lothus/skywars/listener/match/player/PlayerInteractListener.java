package com.lothus.skywars.listener.match.player;

import com.lothus.core.api.actionbar.ActionBar;
import com.lothus.core.games.state.GameState;
import com.lothus.core.servers.type.ServerType;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.menus.CompassMenu;
import com.lothus.skywars.menus.ConfigurationMenu;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.utils.PlayerUtil;
import com.lothus.sync.stats.menus.games.skywars.select.ability.SelectAbilityMenu;
import com.lothus.sync.stats.menus.games.skywars.select.kit.SelectKitMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static com.lothus.core.games.room.RoomType.DUPLAS;
import static com.lothus.core.games.room.RoomType.SOLO;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null) {
            return;
        }

        if (arena.isSpectator(player)) {
            event.setCancelled(true);
        }

        if (arena.isPlayer(player)) {
            if (arena.getState() != GameState.EM_JOGO) {
                event.setCancelled(true);
            }
        }

        if (player.getItemInHand() == null) return;

        ItemStack itemStack = player.getItemInHand();

        if (arena.isPlayer(player) && !arena.isSpectator(player)) {
            if (itemStack.getType() == Material.COMPASS) {
                ArenaTeam team = Platform.getMatch().findTeamByCompass(player);
                if (team == null) {
                    player.sendMessage("§cNão foi possível encontrar times vivos.");
                    return;
                }

                Player find = Bukkit.getPlayer(team.getPlayers().get(0));

                if (find == null) {
                    player.sendMessage("§cNão foi possível encontrar times vivos.");
                    return;
                }

                player.setCompassTarget(find.getLocation());

                player.sendMessage("§eBússola está apontando para §b" + find.getName() + "§e do time §b" + team.getName() + "§e.");
                ActionBar.sendActionBar(player, "§b" + find.getName() + "§e está a §b" + (int) player.getLocation().distance(find.getLocation()) + "§e blocos de distância.");
                return;
            }
        }

        if (!itemStack.hasItemMeta())return;
        if (!itemStack.getItemMeta().hasDisplayName())return;

        if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§cSair")) {
            PlayerUtil.send(player, ServerType.LOBBY_SKYWARS);
            return;
        }

        if (arena.isPlayer(player)) {
            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§aKits")) {
                SelectKitMenu.open(player, arena.getType());
                return;
            }

            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§aHabilidade")) {
                SelectAbilityMenu.open(player, arena.getType());
                return;
            }

        }

        if (arena.isSpectator(player)) {
            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§aLocalizar jogadores")) {
                CompassMenu.open(player);
                return;
            }

            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§aJogar Novamente")) {
                player.chat("/play " + (arena.getType() == SOLO ? "swsolo" : arena.getType() == DUPLAS ? "swteam" : "swranked"));
                return;
            }

            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§aConfigurações")) {
                ConfigurationMenu.open(player);
                return;
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        event.getEntity().setTicksLived(200);
    }

}
