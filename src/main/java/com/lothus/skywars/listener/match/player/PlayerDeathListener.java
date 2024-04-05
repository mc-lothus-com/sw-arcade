package com.lothus.skywars.listener.match.player;

import com.lothus.core.Core;
import com.lothus.core.api.actionbar.ActionBar;
import com.lothus.core.player.LothPlayer;
import com.lothus.skywars.Instance;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.sync.stats.games.addons.deathcries.DeathCry;
import com.lothus.sync.stats.games.addons.slaughter.Slaughter;
import com.lothus.sync.stats.player.games.skywars.SkyPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static com.lothus.core.games.room.RoomType.DUPLAS;
import static com.lothus.core.games.room.RoomType.SOLO;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();
        Arena arena = Platform.getMatch();
        Location deathLocation = player.getLocation().clone();
        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());
        SkyPlayer skyPlayer = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().getAccount(player.getUniqueId());

        if (arena == null) return;

        event.setDeathMessage(null);

        if (killer != null) {
            SkyPlayer k = com.lothus.sync.stats.platform.Platform.getSkyPlatform().getSkyPlayerController().getAccount(killer.getUniqueId());
            GamePlayer g = Platform.getGamePlayerManager().get(killer.getUniqueId());
            LothPlayer killerLothPlayer = Core.getPlayerController().get(killer.getUniqueId());

            killer.sendMessage("§6+" + gamePlayer.coin(1) + " coins!");
            killer.sendMessage("§b+" + gamePlayer.xp(1) + " XP!");

            g.setKills(g.getKills() + 1);

            Slaughter slaughter = com.lothus.sync.stats.platform.Platform.getSlaughterController().getKit(k.getSlaughter());

            arena.getPlayers().forEach(p -> {
                if (slaughter == null) {
                    String km = "{player} §efoi morto por {killer}§e.";
                    p.sendMessage(km.replace(
                                    "{player}",
                                    lothPlayer.getSocial().getFake().getName() != lothPlayer.getName() ? lothPlayer.getSocial().getFake().getRank().getColor() + player.getName() :
                                            lothPlayer.getGroup().getTag().getColor() + player.getName()
                            )
                            .replace("{killer}",
                                    killerLothPlayer.getSocial().getFake().getName() != killerLothPlayer.getName() ? killerLothPlayer.getSocial().getFake().getRank().getColor() + killer.getName() :
                                            killerLothPlayer.getGroup().getTag().getColor() + killer.getName()));
                } else {
                    p.sendMessage(slaughter.message((lothPlayer.getSocial().getFake().getName() != lothPlayer.getName() ? lothPlayer.getSocial().getFake().getRank().getColor() + player.getName() :
                            lothPlayer.getGroup().getTag().getColor() + player.getName()), killerLothPlayer.getSocial().getFake().getName() != killerLothPlayer.getName() ? killerLothPlayer.getSocial().getFake().getRank().getColor() + killer.getName() :
                            killerLothPlayer.getGroup().getTag().getColor() + killer.getName()));
                }
            });
            arena.getSpectators().forEach(p -> {
                if (slaughter == null) {
                    String km = "{player} §efoi morto por {killer}§e.";
                    p.sendMessage(km.replace(
                                    "{player}",
                                    lothPlayer.getSocial().getFake().getName() != lothPlayer.getName() ? lothPlayer.getSocial().getFake().getRank().getColor() + player.getName() :
                                            lothPlayer.getGroup().getTag().getColor() + player.getName()
                            )
                            .replace("{killer}",
                                    killerLothPlayer.getSocial().getFake().getName() != killerLothPlayer.getName() ? killerLothPlayer.getSocial().getFake().getRank().getColor() + killer.getName() :
                                            killerLothPlayer.getGroup().getTag().getColor() + killer.getName()));
                } else {
                    p.sendMessage(slaughter.message((lothPlayer.getSocial().getFake().getName() != lothPlayer.getName() ? lothPlayer.getSocial().getFake().getRank().getColor() + player.getName() :
                            lothPlayer.getGroup().getTag().getColor() + player.getName()), killerLothPlayer.getSocial().getFake().getName() != killerLothPlayer.getName() ? killerLothPlayer.getSocial().getFake().getRank().getColor() + killer.getName() :
                            killerLothPlayer.getGroup().getTag().getColor() + killer.getName()));
                }
            });

            DeathCry d = com.lothus.sync.stats.platform.Platform.getDeathController().getKit(skyPlayer.getDeathCry());

            if (d != null) {
                d.playSound(killer);
            } else {
                killer.playSound(player.getLocation(), Sound.NOTE_PLING, 2.0f, 2.0f);
            }
        } else {
            arena.getPlayers().forEach(p -> {
                p.sendMessage(lothPlayer.getGroup().getTag().getColor() + player.getName() + "§e morreu sozinho.");
            });

            arena.getSpectators().forEach(p -> {
                p.sendMessage(lothPlayer.getGroup().getTag().getColor() + player.getName() + "§e morreu sozinho.");
            });
        }

        gamePlayer.setLose(1);
        gamePlayer.setDeaths(1);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
                player.teleport(deathLocation);
                arena.removePlayer(player);
                arena.addSpectator(player);

                TextComponent t = new TextComponent("§eDeseja jogar novamente?");
                TextComponent click = new TextComponent(" §b§lCLIQUE AQUI!");

                click.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/play " + (arena.getType() == SOLO ? "swsolo" : arena.getType() == DUPLAS ? "swteam" : "swranked")));
                click.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§eClique para jogar!")));

                t.addExtra(click);

                player.sendMessage("");
                player.sendMessage("§cVocê morreu!");
                player.sendMessage(t);
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 2.0f, 2.0f);

                arena.getPlayers().forEach(p -> {
                    p.playSound(p.getLocation(), Sound.SKELETON_DEATH, 2.0f, 2.0f);
                    ActionBar.sendActionBar(p, "§eRestam §b" + arena.getPlayers().size() + "§e jogadores vivos.");
                });
            }
        }.runTaskLater(Instance.getInstance(), 4L);
    }
}
