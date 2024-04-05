package com.lothus.skywars.arena.creator;

import java.util.ArrayList;
import java.util.List;

import com.lothus.core.games.room.RoomType;
import com.lothus.core.utils.bukkit.ItemCreator;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.cube.ArenaCube;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@Getter @Setter
public class ArenaCreator {

   private Player player;

   private String id;
   private String name;
   private RoomType type;

   private World world;

   private Location lobby;
   private Location cubeLobby1;
   private Location cubeLobby2;

   private Location cubeArena1;
   private Location cubeArena2;
   
   private List<Location> spawns = new ArrayList<>();

   private List<Location> chestBasic = new ArrayList<>();
   private List<Location> chestBasic2 = new ArrayList<>();
   private List<Location> chestMiniFeast = new ArrayList<>();
   private List<Location> chestFeast = new ArrayList<>();
   private List<Location> chestRefil = new ArrayList<>();

   public ArenaCreator(Player player) {
      this.player = player;
   }

   public void sendItems() {
      PlayerInventory inventory = player.getInventory();
      ItemCreator spawn = new ItemCreator(Material.SADDLE, "§aAdicionar Spawns");
      ItemCreator entrada = new ItemCreator(Material.BEACON, "§aAdicionar Lobby de Espera");
      ItemCreator cubelobby = new ItemCreator(Material.STICK, "§aAdicionar Cubo do Lobby");
      ItemCreator cube = new ItemCreator(Material.BLAZE_ROD, "§aAdicionar Cubo da Arena");
      
      ItemCreator basic = new ItemCreator(Material.CHEST, "§aBaú Básico 1");
      ItemCreator basic2 = new ItemCreator(Material.CHEST, "§aBaú Básico 2");
      ItemCreator miniFeast = new ItemCreator(Material.CHEST, "§aBaú MiniFeast");
      ItemCreator feast = new ItemCreator(Material.CHEST, "§aBaú Feast");
      ItemCreator ref = new ItemCreator(Material.ENDER_CHEST, "§aBaú Refil");

      ItemCreator wool = new ItemCreator(Material.WOOL, "§aSalvar");

      inventory.setItem(0, spawn.build());
      inventory.setItem(1, entrada.build());
      inventory.setItem(2, cube.build());
      inventory.setItem(3, cubelobby.build());
      inventory.setItem(4, basic.build());
      inventory.setItem(5, basic2.build());
      inventory.setItem(6, miniFeast.build());
      inventory.setItem(7, feast.build());
      inventory.setItem(9, ref.build());
      inventory.setItem(8, wool.build());

      player.setFlySpeed(1f);
   }

   public boolean save() {
      try {
         player.getInventory().clear();
         Arena arena = new Arena(name,lobby);
         arena.setId(id);
         arena.setWorld(lobby.getWorld());
         arena.setType(type);

         arena.setCube(new ArenaCube(cubeArena1, cubeArena2));
         arena.setCubeLobby(new ArenaCube(cubeLobby1, cubeLobby2));

         for (Block block : arena.getCube().getBlocks()) {
            if (block.getType().equals(Material.BEACON)) {
               Location l = block.getLocation().clone();

               l.setY(l.getY() + 1.0);

               spawns.add(l);
            }
         }

         arena.setMaxPlayers(spawns.size());
         arena.setMinPlayers(spawns.size() / 2);

         arena.setSpawns(spawns);

         arena.setChestBasic(chestBasic);
         arena.setChestBasic2(chestBasic2);
         arena.setChestMiniFeast(chestMiniFeast);
         arena.setChestFeast(chestFeast);
         arena.setChestRefil(chestRefil);

         arena.createArenaConfiguration();
         arena.getCubeLobby().save(arena.getWorld().getName(), "arena");
         return true;
      } catch (Exception e) {
         return false;
      }
   }
}