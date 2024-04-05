package com.lothus.skywars.listener.creator;

import com.lothus.core.servers.type.ServerType;
import com.lothus.skywars.arena.creator.ArenaCreator;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.utils.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ArenaCreatorListener implements Listener {

    @EventHandler
    public void onIntect(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ArenaCreator arenaPreset = Platform.getArenaCreatorManager().getArenaCreator(player.getUniqueId());

        if (arenaPreset == null) {
            return;
        }

        if (player.getItemInHand().hasItemMeta()) {
            if (player.getItemInHand().getItemMeta().hasDisplayName()) {
                switch (player.getItemInHand().getItemMeta().getDisplayName()) {
                    case "§aAdicionar Spawns":
                        e.setCancelled(true);
                        if (arenaPreset.getSpawns().contains(player.getLocation())) {
                            player.sendMessage("§cJá existe um spawn setado nessa localização!");
                            return;
                        }

                        arenaPreset.getSpawns().add(player.getLocation());
                        player.sendMessage("§aVocê adicionou uma novo spawn nesta localização, quantidade atual §f" + arenaPreset.getSpawns().size());
                        break;
                    case "§aAdicionar Lobby de Espera":
                        e.setCancelled(true);
                        arenaPreset.setLobby(player.getLocation());
                        arenaPreset.setWorld(player.getLocation().getWorld());
                        player.sendMessage("§aVocê definiu o spawn de entrada deste jogo!");
                        break;
                    case "§aAdicionar Cubo do Lobby":
                        e.setCancelled(true);
                        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            arenaPreset.setCubeLobby1(e.getClickedBlock().getLocation());
                            player.sendMessage("§aVocê definiu a posição §f1§a do cubo do lobby!");
                        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            arenaPreset.setCubeLobby2(e.getClickedBlock().getLocation());
                            player.sendMessage("§aVocê definiu a posição §f2§a do cubo do lobby!");
                        }
                        break;
                    case "§aAdicionar Cubo da Arena":
                        e.setCancelled(true);
                        if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            arenaPreset.setCubeArena1(e.getClickedBlock().getLocation());
                            player.sendMessage("§aVocê definiu a posição §f1§a do cubo da arena!");
                        } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            arenaPreset.setCubeArena2(e.getClickedBlock().getLocation());
                            player.sendMessage("§aVocê definiu a posição §f2§a do cubo da arena!");
                        }
                        break;
                    case "§aBaú Básico 1":
                        e.setCancelled(true);

                        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
                            player.sendMessage("§cVocê deve clicar em um bloco.");
                            return;
                        }

                        arenaPreset.getChestBasic().add(e.getClickedBlock().getLocation());
                        player.sendMessage("§aVocê adicionou um Baú Básico. Atualmente existem §f" + arenaPreset.getChestBasic().size() + "§a baús.");
                        break;
                    case "§aBaú Básico 2":
                        e.setCancelled(true);

                        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
                            player.sendMessage("§cVocê deve clicar em um bloco.");
                            return;
                        }

                        arenaPreset.getChestBasic2().add(e.getClickedBlock().getLocation());
                        player.sendMessage("§aVocê adicionou um Baú Básico 2. Atualmente existem §f" + arenaPreset.getChestBasic2().size() + "§a baús.");
                        break;
                    case "§aBaú MiniFeast":
                        e.setCancelled(true);

                        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
                            player.sendMessage("§cVocê deve clicar em um bloco.");
                            return;
                        }

                        arenaPreset.getChestMiniFeast().add(e.getClickedBlock().getLocation());
                        player.sendMessage("§aVocê adicionou um Baú MiniFeast. Atualmente existem §f" + arenaPreset.getChestMiniFeast().size() + "§a baús.");
                        break;
                    case "§aBaú Feast":
                        e.setCancelled(true);

                        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
                            player.sendMessage("§cVocê deve clicar em um bloco.");
                            return;
                        }

                        arenaPreset.getChestFeast().add(e.getClickedBlock().getLocation());
                        player.sendMessage("§aVocê adicionou um Baú Feast. Atualmente existem §f" + arenaPreset.getChestFeast().size() + "§a baús.");
                        break;
                    case "§aBaú Refil":
                        e.setCancelled(true);

                        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().equals(Material.CHEST)) {
                            player.sendMessage("§cVocê deve clicar em um bloco.");
                            return;
                        }

                        arenaPreset.getChestRefil().add(e.getClickedBlock().getLocation());
                        player.sendMessage("§aVocê adicionou um Baú Refil. Atualmente existem §f" + arenaPreset.getChestRefil().size() + "§a baús.");
                        break;
                    case "§aSalvar":
                        e.setCancelled(true);
                        player.sendMessage("§eSalvando arena...");
                        player.setFlySpeed(0.1F);
                        PlayerUtil.send(player, ServerType.LOBBY);
                        arenaPreset.save();
                }
            }
        }
    }
}
