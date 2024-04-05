package com.lothus.skywars.listener.match.chat;

import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import com.lothus.sync.stats.player.games.skywars.SkyPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncChatListener implements Listener {

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        event.setCancelled(true);
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());

        if (arena == null) {
            event.setCancelled(true);
            return;
        }

        if (!arena.isPlayer(player) && !arena.isSpectator(player)) {
            event.setCancelled(true);
            return;
        }

        SkyPlayer s = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().getAccount(player.getUniqueId());
        if (arena.isSpectator(player)) {
            arena.getSpectators().forEach(spectator -> {
                if (lothPlayer.getSocial().getFake() == null || lothPlayer.getSocial().getFake().getName().equalsIgnoreCase(lothPlayer.getName())) {
                    spectator.sendMessage("§8[E] §7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getGroup().getTag() == Rank.MEMBRO ? "§7" : lothPlayer.getGroup().getTag().getColor() + "§l" + lothPlayer.getGroup().getTag().getName().toUpperCase() + " " + lothPlayer.getGroup().getTag().getColor()) + player.getName() + ": §7" + event.getMessage());
                } else {
                    spectator.sendMessage("§8[E] §7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getSocial().getFake().getRank() == Rank.MEMBRO ? "§7" : lothPlayer.getSocial().getFake().getRank().getColor() + "§l" + lothPlayer.getSocial().getFake().getRank().getName().toUpperCase() + " " + lothPlayer.getSocial().getFake().getRank().getColor()) + player.getName() + ": §7" + event.getMessage());
                }
            });
            return;
        }

        if (arena.isPlayer(player)) {
            arena.getPlayers().forEach(p -> {
                if (lothPlayer.getSocial().getFake() == null || lothPlayer.getSocial().getFake().getName().equalsIgnoreCase(lothPlayer.getName())) {
                    p.sendMessage("§7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getGroup().getTag() == Rank.MEMBRO ? "§7" : lothPlayer.getGroup().getTag().getColor() + "§l" + lothPlayer.getGroup().getTag().getName().toUpperCase() + " " + lothPlayer.getGroup().getTag().getColor()) + player.getName() + ": §7" + event.getMessage());
                } else {
                    p.sendMessage("§7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getSocial().getFake().getRank() == Rank.MEMBRO ? "§7" : lothPlayer.getSocial().getFake().getRank().getColor() + "§l" + lothPlayer.getSocial().getFake().getRank().getName().toUpperCase() + " " + lothPlayer.getSocial().getFake().getRank().getColor()) + player.getName() + ": §7" + event.getMessage());
                }
            });
            arena.getSpectators().forEach(spectator -> {
                if (lothPlayer.getSocial().getFake() == null || lothPlayer.getSocial().getFake().getName().equalsIgnoreCase(lothPlayer.getName())) {
                    spectator.sendMessage("§7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getGroup().getTag() == Rank.MEMBRO ? "§7" : lothPlayer.getGroup().getTag().getColor() + "§l" + lothPlayer.getGroup().getTag().getName().toUpperCase() + " " + lothPlayer.getGroup().getTag().getColor()) + player.getName() + ": §7" + event.getMessage());
                } else {
                    spectator.sendMessage("§7[" + s.getLevel() + "✰] " + lothPlayer.getMedal().getColor() + lothPlayer.getMedal().getSymbol() + " " + (lothPlayer.getSocial().getFake().getRank() == Rank.MEMBRO ? "§7" : lothPlayer.getSocial().getFake().getRank().getColor() + "§l" + lothPlayer.getSocial().getFake().getRank().getName().toUpperCase() + " " + lothPlayer.getSocial().getFake().getRank().getColor()) + player.getName() + ": §7" + event.getMessage());
                }
            });

        }
    }
}
