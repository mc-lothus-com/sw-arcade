package com.lothus.skywars.listener.arena;

import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.event.task.ArenaPlayingEvent;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.skywars.scoreboard.ArenaScoreboard;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static com.lothus.core.games.room.RoomType.DUPLAS;
import static com.lothus.core.games.room.RoomType.SOLO;

public class ArenaPlayingListener implements Listener {

    private boolean e = false;

    @EventHandler
    public void onPlaying(ArenaPlayingEvent event) {
        Arena arena = event.getArena();
        Long time = arena.getTask().getTime();

        arena.updateTags();

        arena.updateGameInfo();

        if (arena.getPlayers().size() < 1) {
            arena.getTask().setTime(2);
            arena.setState(GameState.REINICIANDO);
            return;
        }

        if (time == 900) {
            arena.getCubeLobby().getBlocks().forEach(b -> b.setType(Material.AIR));
        }

        if (arena.getPlayers().size() <= arena.getType().getMaxPlayersPerTeam()) {
            if (arena.getType().getMaxPlayersPerTeam() <= 1) {
                arena.getPlayers().forEach(player -> {
                    GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
                    TextComponent t = new TextComponent("§eDeseja jogar novamente?");
                    TextComponent click = new TextComponent(" §b§lCLIQUE AQUI!");

                    click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play " + (arena.getType() == SOLO ? "swsolo" : arena.getType() == DUPLAS ? "swteam" : "swranked")));
                    click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClique para jogar!")));

                    t.addExtra(click);

                    player.sendMessage("");
                    player.sendMessage("§aVocê venceu a partida!");
                    player.sendMessage(t);
                    player.sendMessage("");

                    gamePlayer.setWins(1);

                    if (!e) {
                        EnderDragon dragon = (EnderDragon) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDER_DRAGON);
                        dragon.setPassenger(player);
                        e = true;
                    }

                    ArenaTeam team = arena.getTeam(player);

                    arena.setWinner(team);
                    arena.getTask().setDamage(false);
                    gamePlayer.updateStats();
                });

                arena.setState(GameState.ENCERRANDO);
                arena.getTask().setTime(10);
            }

            if (arena.getType().getMaxPlayersPerTeam() <= 2) {
                if (arena.getPlayers().size() == 1) {
                    arena.getTask().setTime(10);

                    arena.setState(GameState.ENCERRANDO);
                    return;
                }

                ArenaTeam t1 = arena.getTeam(arena.getPlayers().get(0));
                ArenaTeam t2 = arena.getTeam(arena.getPlayers().get(1));

                if (t1 == t2) {
                    arena.setWinner(t1);
                    arena.getPlayers().forEach(player -> {
                        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
                        TextComponent t = new TextComponent("§eDeseja jogar novamente?");
                        TextComponent click = new TextComponent(" §b§lCLIQUE AQUI!");

                        click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play " + (arena.getType() == SOLO ? "swsolo" : arena.getType() == DUPLAS ? "swteam" : "swranked")));
                        click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClique para jogar!")));

                        t.addExtra(click);

                        player.sendMessage("");
                        player.sendMessage("§aVocê venceu a partida!");
                        player.sendMessage(t);
                        player.sendMessage("");

                        gamePlayer.setWins(1);

                        if (!e) {
                            EnderDragon dragon = (EnderDragon) player.getWorld().spawnEntity(player.getLocation(), EntityType.ENDER_DRAGON);
                            dragon.setPassenger(player);
                            e = true;
                        }

                        ArenaTeam team = arena.getTeam(player);

                        arena.setWinner(team);
                        arena.getTask().setDamage(false);
                        gamePlayer.updateStats();
                    });
                    arena.setState(GameState.ENCERRANDO);
                    arena.getTask().setTime(10);
                }
            }
        }

        if (time == 900) {
            arena.getPlayers().forEach(player -> {
                player.sendMessage("");
                player.sendMessage("§c§lAVISO:");
                player.sendMessage(" §c- A aliança com outros jogadores é proibida e resultará em §lpunição§c.");
                player.sendMessage(" §c- O uso de §lbugs§c e §ltrapaças§c é proibido e resultará em §lpunição§c.");
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 2.0f);
            });
        }

        if (time == 899) {
            arena.getTask().setDamage(true);
        }

        if (time == 610 || time == 550) {
            arena.getChestRefil().forEach(location -> {
                location.getBlock().setType(Material.CHEST);
                Platform.getChestManager().applyRefil(location);
            });
            arena.getPlayers().forEach(player -> {
                player.sendMessage("§aTodos os baús foram preenchidos.");
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            });
        }

        if (time < 426 && time > 420) {
            arena.getPlayers().forEach(player -> {
                player.sendMessage("§aA borda será reduzida em §f" + arena.getBorderFirstSize() + " §ablocos em §f" + (time == 425 ? "5" : time == 424 ? "4" : time == 423 ? "3" : time == 422 ? "2" : "1") + "§a segundo" + (time > 421 ? "s" : "") + ".");
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            });
            arena.getSpectators().forEach(player -> {
                player.sendMessage("§aA borda será reduzida em §f" + arena.getBorderFirstSize() + " §ablocos em §f" + (time == 425 ? "5" : time == 424 ? "4" : time == 423 ? "3" : time == 422 ? "2" : "1") + "§a segundo" + (time > 421 ? "s" : "") + ".");
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            });
        }

        if (time < 366 && time > 360) {
            arena.getPlayers().forEach(player -> {
                player.sendMessage("§aA borda será reduzida em §f" + arena.getBorderSecondSize() + " §ablocos em §f" + (time == 365 ? "5" : time == 364 ? "4" : time == 363 ? "3" : time == 362 ? "2" : "1") + "§a segundo" + (time > 361 ? "s" : "") + ".");
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            });
            arena.getSpectators().forEach(player -> {
                player.sendMessage("§aA borda será reduzida em §f" + arena.getBorderSecondSize() + " §ablocos em §f" + (time == 365 ? "5" : time == 364 ? "4" : time == 363 ? "3" : time == 362 ? "2" : "1") + "§a segundo" + (time > 361 ? "s" : "") + ".");
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
            });
        }

        if (time == 420 || time == 360) {
            arena.setBorder(time == 420 ? arena.getBorderFirstSize() : arena.getBorderSecondSize());
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aA borda foi alterada para um raio de §f" + (time == 420 ? arena.getBorderFirstSize() : arena.getBorderSecondSize()) + " §ablocos.");
            });
            arena.getSpectators().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aA borda foi alterada para um raio de §f" + (time == 420 ? arena.getBorderFirstSize() : arena.getBorderSecondSize()) + " §ablocos.");
            });
        }
        if (time == 335 || time == 334 || time == 333 || time == 332 || time == 331) {
            arena.setBorder(arena.getBorderLastSize());
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aA borda será reduzida em §f" + arena.getBorderLastSize() + " §ablocos em §f" + (time == 335 ? "5" : time == 334 ? "4" : time == 333 ? "3" : time == 332 ? "2" : "1") + "§a segundo" + (time > 331 ? "s" : "") + ".");
            });
        }
        if (time == 330) {
            arena.setBorder(arena.getBorderLastSize());
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aA borda foi alterada para um raio de §f" + arena.getBorderLastSize() + " §ablocos.");
            });
        }

        if (time < 186 && time > 180) {
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.CLICK, 2.0f, 2.0f);
                player.sendMessage("§aO cão de guarda terá liberdade em §f" + (time == 185 ? "5" : time == 184 ? "4" : time == 183 ? "3" : time == 182 ? "2" : "1") + "§a segundos!");
            });
        }

        if (time == 0 || time == 60 || time == 120 || time == 180) {
            EnderDragon dragon = arena.getLobby().getWorld().spawn(arena.getLobby(), EnderDragon.class);
            dragon.setHealth(dragon.getMaxHealth());
            arena.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 2.0f, 2.0f);
                player.sendMessage((time == 0 ? "§aO cão de guarda" : "§a+§f1 §acão de guarda") + " foi solto!");
            });
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
