package com.lothus.skywars.listener.match.player;

import com.lothus.core.Core;
import com.lothus.core.games.state.GameState;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());
            GamePlayer gamePlayer = new GamePlayer(player.getUniqueId());

            Platform.getGamePlayerManager().load(gamePlayer);
            Arena arena = Platform.getMatch();

            if (arena == null) {
                if (lothPlayer.getGroup().getRank() != Rank.CEO) {
                    if (!lothPlayer.getGroup().containsPermission("skywars.admin")) {
                        player.kickPlayer("§cNão foi possível encontrar sua arena.");
                        return;
                    }
                }
            } else {
                joinArena(player);
            }

        } catch (Exception ex) {
            player.kickPlayer("§cOcorreu um erro ao conectar-se.");
            event.setJoinMessage(null);
            return;
        }
    }

    private void joinArena(Player player) {
        Arena arena = Platform.getMatch();
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());
        ArenaScoreboard scoreboard = new ArenaScoreboard(player);
        scoreboard.create();

        if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
            if (arena.getState() == GameState.INICIANDO || arena.getState().equals(GameState.PREPARANDO)) {
                player.kickPlayer("§cA partida está cheia.");
                return;
            }
        }

        if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
            arena.addSpectator(player);
            player.teleport(arena.getLobby().add(0, 1.0, 0));
            arena.getLobby().subtract(0, 1.0, 0);
            player.sendMessage("§eA partida está §ccheia§e, portanto você está como espectador.");
            return;
        }

        if (arena.getState() == GameState.PREPARANDO || arena.getState() == GameState.EM_JOGO || arena.getState() == GameState.ENCERRANDO) {
            arena.addSpectator(player);
            player.teleport(arena.getLobby().add(0, 1.0, 0));
            arena.getLobby().subtract(0, 1.0, 0);
            player.sendMessage("§eA partida já §ciniciou§e, portanto você está como espectador.");
            return;
        }

        if (lothPlayer.getPrefs().isVanish()) {
            arena.addSpectator(player);
            player.teleport(arena.getLobby().add(0, 1.0, 0));
            arena.getLobby().subtract(0, 1.0, 0);
            player.sendMessage("§eVocê está no modo §d§lADMIN§e, portanto você está como espectador.");
            return;
        }

        Platform.getScoreboardManager().load(player.getUniqueId(), scoreboard);
        arena.addPlayer(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null) {
            if (Platform.getArenaCreatorManager().getArenaCreator(player.getUniqueId()) != null) {
                Platform.getArenaCreatorManager().unload(player.getUniqueId());
            }
            Platform.getGamePlayerManager().unload(player.getUniqueId());
            return;
        }

        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());

        if (gamePlayer != null) {
            if (arena.getWinner() != null) {
                if (!arena.getWinner().isPlayer(player.getUniqueId())) {
                    gamePlayer.updateStats();
                }
            }
        }

        if (arena.isPlayer(player)) {
            arena.removePlayer(player);
        }

        if (arena.isSpectator(player)) {
            arena.removeSpectator(player);
        }

        if (Platform.getArenaCreatorManager().getArenaCreator(player.getUniqueId()) != null) {
            Platform.getArenaCreatorManager().unload(player.getUniqueId());
        }

        Platform.getScoreboardManager().unload(player.getUniqueId());
        Platform.getGamePlayerManager().unload(player.getUniqueId());
    }
}
