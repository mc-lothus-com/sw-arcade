package com.lothus.skywars.listener.arena;

import com.lothus.core.Core;
import com.lothus.core.games.room.RoomType;
import com.lothus.core.games.state.GameState;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.servers.ServerInfo;
import com.lothus.core.servers.type.ServerType;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.event.task.ArenaTurningOffEvent;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import com.lothus.skywars.utils.PlayerUtil;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;

import java.util.Comparator;
import java.util.List;

public class ArenaTurningOffListener implements Listener {

    @EventHandler
    public void onArenaTurningOff(ArenaTurningOffEvent event) {
        Arena arena = event.getArena();
        Long time = arena.getTask().getTime();

        arena.updateTags();
        arena.updateGameInfo();

        for (Player player : arena.getPlayers()) {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

            if (arenaScoreboard == null)continue;

            arenaScoreboard.update();
        }

        for (Player player : arena.getSpectators()) {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

            if (arenaScoreboard == null)continue;

            arenaScoreboard.update();
        }

        if (time <= 10 && time > 0) {
            if (arena.getType() == RoomType.SOLO) {
                arena.getPlayers().forEach(player -> {
                    Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    firework.getFireworkMeta().addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.YELLOW).withColor(Color.GREEN).withColor(Color.BLUE).withColor(Color.PURPLE).with(FireworkEffect.Type.BALL_LARGE).build());
                    player.sendTitle(new Title("§6§lVITÓRIA", "§eVocê venceu essa partida!", 0, 20, 0));
                    arena.setWinner(arena.getTeam(player));
                });
            } else if (arena.getType() == RoomType.DUPLAS) {
                arena.getPlayers().forEach(player -> {
                    Firework firework = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                    firework.getFireworkMeta().addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.YELLOW).withColor(Color.GREEN).withColor(Color.BLUE).withColor(Color.PURPLE).with(FireworkEffect.Type.BALL_LARGE).build());
                    player.sendTitle(new Title("§6§lVITÓRIA", "§eO seu time venceu essa partida!", 0, 20, 0));
                    arena.setWinner(arena.getTeam(player));
                });
            }
            arena.getSpectators().forEach(player -> {
                LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());
                if (lothPlayer == null)return;
                if (!lothPlayer.getPrefs().isVanish()) {
                    player.sendTitle(new Title("§4§lDERROTA", "§eNão foi dessa vez, tente novamente!", 0, 20, 0));
                } else {
                    player.sendTitle(new Title("§c§lFIM DE JOGO", "§eA partida acabou!", 0, 20, 0));
                }
            });
        }

        if (time == 0) {
            arena.getPlayers().forEach(player -> {
                PlayerUtil.send(player, ServerType.LOBBY_SKYWARS);
            });

            arena.getSpectators().forEach(player -> {
                GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
                if (gamePlayer ==null)return;

                gamePlayer.updateStats();
                com.lothus.core.utils.bukkit.player.PlayerUtil.connect(player.getUniqueId(), getServerInfo(ServerType.LOBBY_SKYWARS));
            });
            arena.getTask().setTime(5);
            arena.setState(GameState.REINICIANDO);
        }
    }

    private ServerInfo getServerInfo(ServerType type) {
        Comparator<ServerInfo> comparator = Comparator.comparing(ServerInfo::getPlayers);
        List<ServerInfo> list = Core.getServerController().get(type);
        list.sort(comparator);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
