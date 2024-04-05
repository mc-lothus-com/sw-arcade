package com.lothus.skywars.listener.match.player;

import com.lothus.bukkit.events.commands.AdminChangeEvent;
import com.lothus.core.Core;
import com.lothus.core.event.update.UpdateEvent;
import com.lothus.core.player.LothPlayer;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import com.lothus.sync.stats.data.type.DataType;
import com.lothus.sync.stats.player.games.skywars.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class PlayerUpdateListener implements Listener {


    @EventHandler
    public void onUpdate(UpdateEvent event) {
        Player player = event.getPlayer();
        Arena arena = Platform.getMatch();

        if (arena == null)return;

        SkyPlayer skyPlayer = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().getAccount(player.getUniqueId());
        if (skyPlayer != null && skyPlayer.getXp() >= 500) {
            skyPlayer.setLevel(skyPlayer.getLevel() + 1);
            skyPlayer.setXp(skyPlayer.getXp() - 500);

            player.sendMessage("");
            player.sendMessage("§eVocê subiu para o nível §b["+ skyPlayer.getLevel() + "✰]§e!");
            player.sendMessage("");

            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
            com.lothus.sync.stats.platform.Platform.getDataPlayer().update(DataType.SKY_WARS_ACCOUNT, skyPlayer);
            return;
        }

        updateHealthBar(player);

        for (Player p : arena.getSpectators()) {
            if (arena.isPlayer(p)) {
                arena.getPlayers().remove(player);
            }
        }
    }

    @EventHandler
    public void onAdminChage(AdminChangeEvent event) {
        Player player = event.getPlayer();
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());

        for (Player o : Bukkit.getOnlinePlayers()) {
            if (event.isStatus()) {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 4.0f, 4.0f);
                o.hidePlayer(player);
            } else {
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 4.0f, 4.0f);
                o.showPlayer(player);
            }
        }

        lothPlayer.getPrefs().setVanish(event.isStatus());
        player.sendMessage("§b§lADMIN §8-> §eVocê " + (event.isStatus() ? "§a§lENTROU" : "§c§lSAIU") + " §edo modo admin.");
        Core.getDataPlayer().update(lothPlayer);
    }

    private void updateHealthBar(Player player) {
        Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

        if (objective == null) {
            objective = player.getScoreboard().registerNewObjective("showhealth", "health");
            objective.setDisplayName("§c❤");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
    }
}
