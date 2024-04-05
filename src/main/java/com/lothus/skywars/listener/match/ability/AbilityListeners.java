package com.lothus.skywars.listener.match.ability;

import com.lothus.core.games.room.RoomType;
import com.lothus.core.games.state.GameState;
import com.lothus.core.utils.bukkit.ItemCreator;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import com.lothus.sync.stats.data.type.DataType;
import com.lothus.sync.stats.games.addons.ability.register.LuckyClover;
import com.lothus.sync.stats.games.addons.ability.register.Viper;
import com.lothus.sync.stats.player.games.skywars.stats.SkyStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.lothus.core.games.room.RoomType.SOLO;

public class AbilityListeners implements Listener {
    
    @EventHandler
    public void onLuckyClover(LuckyClover.LuckyCloverReceiveEvent event) {
        Arena arena = Platform.getMatch();
        Player player = event.getPlayer();

        if (arena.getType() == SOLO) {
            SkyStats solo = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().get(DataType.SKY_WARS_SOLO, player.getUniqueId());
            if (solo.getAbility().equalsIgnoreCase("LuckyClover")) {
                player.getInventory().addItem(new ItemCreator(Material.GOLDEN_APPLE).build());
            }
        } else if (arena.getType() == RoomType.DUPLAS) {
            SkyStats team = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().get(DataType.SKY_WARS_TEAM, player.getUniqueId());
            if (team.getAbility().equalsIgnoreCase("LuckyClover")) {
                player.getInventory().addItem(new ItemCreator(Material.GOLDEN_APPLE).build());
            }
        }
    }

    @EventHandler
    public void onViper(Viper.ViperUseEvent event) {
        Arena arena = Platform.getMatch();
        Player player = event.getPlayer();
        Player damager = event.getPoisoned();

        if (arena.getState() != GameState.EM_JOGO)return;

        SkyStats stats = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().get((arena.getType() == SOLO ? DataType.SKY_WARS_SOLO : DataType.SKY_WARS_TEAM), damager.getUniqueId());

        if (stats == null) {
            return;
        }

        if (!stats.getAbility().equals("Viper")) {
            return;
        }

        damager.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20*4, 1), true);

        damager.sendMessage("§b§lVIPER: §eO seu inimigo foi envenenado.");
        player.sendMessage("§b§lVIPER: §eVocê foi envenenado por §b" + damager.getName() + "§e.");
    }
}
