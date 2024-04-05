package com.lothus.skywars.listener.arena;

import com.lothus.core.games.room.RoomType;
import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.chests.type.ChestType;
import com.lothus.skywars.event.task.ArenaPreparingEvent;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import com.lothus.sync.stats.data.type.DataType;
import com.lothus.sync.stats.games.addons.kit.Kit;
import com.lothus.sync.stats.player.games.skywars.stats.SkyStats;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;

public class ArenaPreparingListener implements Listener {

    @EventHandler
    public void onPreparingArena(ArenaPreparingEvent event) {
        Arena arena = event.getArena();
        Long time = arena.getTask().getTime();

        arena.updateGameInfo();
        if (time == 0) {
            arena.getTask().setTime(902);

            arena.getSpawns().forEach(s -> {
                Location a = s.clone();
                a.add(0,0.500,0);
                arena.removeCages();
            });

            arena.setState(GameState.EM_JOGO);

            arena.getChestBasic().forEach(location -> {
                location.getBlock().setType(Material.CHEST);
                Platform.getChestManager().applyChest(ChestType.BASIC, location);
            });
            arena.getChestBasic2().forEach(location -> {
                location.getBlock().setType(Material.CHEST);
                Platform.getChestManager().applyChest(ChestType.BASIC_2, location);
            });
            arena.getChestMiniFeast().forEach(location -> {
                location.getBlock().setType(Material.CHEST);
                Platform.getChestManager().applyChest(ChestType.MINI_FEAST, location);
            });
            arena.getChestFeast().forEach(location -> {
                location.getBlock().setType(Material.CHEST);
                Platform.getChestManager().applyChest(ChestType.FEAST, location);
            });

            arena.getPlayers().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;

                arenaScoreboard.clearLines();
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
                player.sendTitle(new Title("§6§lSKY WARS", "§eComeçou!", 0, 20, 0));

                if (arena.getType() == RoomType.SOLO) {
                    SkyStats solo = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().get(DataType.SKY_WARS_SOLO, player.getUniqueId());
                    if (!solo.getKit().equalsIgnoreCase("None")) {
                        Kit kit = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getKitController().getKit(solo.getKit());
                        kit.apply(player);
                    }
                }

                if (arena.getType() == RoomType.DUPLAS) {
                    SkyStats team = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().get(DataType.SKY_WARS_TEAM, player.getUniqueId());
                    if (!team.getKit().equalsIgnoreCase("None")) {
                        Kit kit = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getKitController().getKit(team.getKit());
                        kit.apply(player);
                    }
                }
            });

            arena.getSpectators().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;

                arenaScoreboard.clearLines();
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
                player.sendTitle(new Title("§6§lSKY WARS", "§eComeçou!", 0, 20, 0));
            });

            arena.getTask().setDamage(false);
        }
        arena.getPlayers().forEach(p -> {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(p.getUniqueId());

            if (arenaScoreboard == null) return;

            arenaScoreboard.update();
        });
    }
}
