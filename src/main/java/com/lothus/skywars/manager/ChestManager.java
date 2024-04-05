package com.lothus.skywars.manager;

import java.util.*;

import com.lothus.core.Core;
import com.lothus.core.games.room.RoomType;
import com.lothus.skywars.Instance;
import com.lothus.skywars.arena.chests.ChestInfo;
import com.lothus.skywars.arena.chests.item.ChestItem;
import com.lothus.skywars.arena.chests.type.ChestType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestManager {
   private final List<ChestInfo> chests = new ArrayList<>();
   public void applyChest(ChestType chestType, Location location) {
      Chest chest = (Chest) location.getBlock().getState();
      Inventory inventory = chest.getInventory();

      Random random = new Random();
      for (ChestInfo chestInfo : chests) {
         if (!(chestInfo.getType() == chestType)) continue;

         for (ChestItem chestItem : chestInfo.getItems()) {
            int slot = random.nextInt(inventory.getSize());
            int percent = random.nextInt(101);
            int itemPercent = chestItem.getPercent();

            if (itemPercent != 0 && percent < itemPercent)continue;

            if (inventory.getItem(slot) != null && chestType.getFinder().startsWith("basic")) {
               inventory.addItem(chestItem.getItemStack());
            } else {
               inventory.setItem(slot, chestItem.getItemStack());
            }
         }
      }
   }

   public void applyRefil(Location location) {
      Chest chest = (Chest) location.getBlock().getState();
      Inventory inventory = chest.getInventory();
      inventory.clear();
      Random random = new Random();
      for (ChestInfo chestInfo : chests) {
         if ((chestInfo.getType() != ChestType.REFIL) && chestInfo.getType() != ChestType.FEAST) continue;

         for (ChestItem chestItem : chestInfo.getItems()) {
            int slot = random.nextInt(inventory.getSize());
            int percent = random.nextInt(101);
            int itemPercent = chestItem.getPercent();

            if (itemPercent != 0 && percent < itemPercent) continue;

            inventory.setItem(slot, chestItem.getItemStack());
         }
      }
   }

   public void loadItems() {
      FileConfiguration configuration = Instance.getInstance().getConfig();
      for (String arenaType : configuration.getConfigurationSection("chests").getKeys(false)) {
         if (RoomType.getRoomType(arenaType) == null) {
            continue;
         }

         for (ChestType chestType : ChestType.values()) {
            ChestInfo chestInfo = new ChestInfo(chestType);
            chestInfo.setItems(getItem(configuration, arenaType, chestType.getFinder()));
            chests.add(chestInfo);
         }
      }
   }

   private List<ChestItem> getItem(Configuration configuration, String arenaType, String type) {
      List<ChestItem> items = new ArrayList<>();
      for (String material : configuration.getConfigurationSection("chests." + arenaType + "." + type).getKeys(false)) {
         Material m = Material.getMaterial(material);

         if (m == null) {
            Core.getLogger().info(material + " não é um material válido.");
            continue;
         }

         int percent = configuration.getInt("chests." + arenaType + "." +type+ "." + material + ".percent");
         ItemStack i = new ItemStack(m);

         i.setAmount(
                 configuration.getInt("chests." + arenaType + "." +type+ "." + material + ".amount")
         );

         i.setDurability(
                 (short)configuration.getInt("chests." + arenaType + "." +type+ "." + material + ".data")
         );

         for (String e : configuration.getConfigurationSection("chests." + arenaType + "." +type+ "." + material + ".enchants").getKeys(false)) {
            Enchantment enchantment = Enchantment.getByName(e);

            if (enchantment == null) {
               Core.getLogger().info(e + " é um encantamento inválido.");
            } else {
               i.addEnchantment(enchantment, configuration.getInt("chests." + arenaType + "." +type+ "." + material + ".enchants." + e + ".level"));
            }
         }

         items.add(new ChestItem(i, percent));
      }
      return items;
   }

}