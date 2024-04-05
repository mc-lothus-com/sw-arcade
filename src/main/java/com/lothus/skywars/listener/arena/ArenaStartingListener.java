package com.lothus.skywars.listener.arena;

import com.lothus.core.api.tag.TagManager;
import com.lothus.core.games.state.GameState;
import com.lothus.skywars.Instance;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.event.task.ArenaStartingEvent;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import com.lothus.skywars.utils.SchemLoader;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.github.paperspigot.Title;

import java.io.File;

public class ArenaStartingListener implements Listener { 
    
    @EventHandler
    public void onStarting(ArenaStartingEvent event) {
        Arena arena = event.getArena();
        Long time = arena.getTask().getTime();

        arena.updateGameInfo();

        Bukkit.getOnlinePlayers().forEach(TagManager::update);
        if (arena.getPlayers().size() < arena.getMinPlayers()) {
            arena.getPlayers().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;

                arenaScoreboard.clearLines();
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2.0f, 2.0f);
                player.sendMessage("§cO jogo foi cancelado por falta de jogadores!");
            });
            arena.getSpectators().forEach(player -> {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard == null) return;

                arenaScoreboard.clearLines();
                arenaScoreboard.update();
                player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 2.0f, 2.0f);
                player.sendMessage("§cO jogo foi cancelado por falta de jogadores!");
            });
            arena.setState(GameState.ESPERANDO);
        }

        if (time == 30 || time == 15 || time < 11 && time > 0) {
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 0, 20, 0));
            });
            arena.getSpectators().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO jogo vai começar em §f" + time + "§a segundos!");
                player.sendTitle(new Title("§6§lSKY WARS", "§eIniciando em §6" + time + " §esegundos!", 0, 20, 0));
            });
        }
        if (time == 5) {
            arena.getSpawns().forEach(s -> {
                SchemLoader.paste(new File(Instance.getInstance().getDataFolder().getPath() + "/cages/default.schematic"), s);
            });
        }

        if (time == 0) {
            arena.getTask().setTime(5);

            arena.createTeams();

            arena.setState(GameState.PREPARANDO);
            for (Player player : arena.getPlayers()) {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard != null) {
                    arenaScoreboard.clearLines();
                    arenaScoreboard.update();
                }

                ArenaTeam team = arena.getTeam(player);
                Location location = team.getSpawn().clone();

                location.setX(location.getX() + 0.600);
                location.setZ(location.getZ() + 0.600);

                player.teleport(location);

                player.getInventory().clear();
                player.getActivePotionEffects().forEach(p -> {
                    player.removePotionEffect(p.getType());
                });

                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);

                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
                player.sendTitle(new Title("§6§lSKY WARS", "§ePrepare-se!", 20, 40, 20));
            }

            for (Player player : arena.getSpectators()) {
                ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(player.getUniqueId());

                if (arenaScoreboard != null) {
                    arenaScoreboard.clearLines();
                    arenaScoreboard.update();
                }

                player.getInventory().clear();
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
                player.sendTitle(new Title("§6§lSKY WARS", "§ePrepare-se!", 20, 40, 20));
            }

            arena.getCubeLobby().getBlocks().forEach(block -> block.setType(Material.AIR));
        }
        arena.getPlayers().forEach(p -> {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(p.getUniqueId());

            if (arenaScoreboard == null) return;

            arenaScoreboard.update();
        });

        arena.getSpectators().forEach(p -> {
            ArenaScoreboard arenaScoreboard = Platform.getScoreboardManager().getScoreboard(p.getUniqueId());

            if (arenaScoreboard == null) return;

            arenaScoreboard.update();
        });
    }

}
