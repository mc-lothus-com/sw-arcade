package com.lothus.skywars.menus;

import com.lothus.core.utils.bukkit.ItemCreator;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ConfigurationMenu implements Listener {

    public static void open(Player player) {
        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
        Inventory inventory = Bukkit.createInventory(null, 9*4, "Configurações");

        inventory.setItem(11, new ItemCreator(Material.LEATHER_BOOTS, "§aSem Velocidade").build());
        inventory.setItem(12, new ItemCreator(Material.CHAINMAIL_BOOTS, "§aVelocidade I").build());
        inventory.setItem(13, new ItemCreator(Material.IRON_BOOTS, "§aVelocidade II").build());
        inventory.setItem(14, new ItemCreator(Material.GOLD_BOOTS, "§aVelocidade III").build());
        inventory.setItem(15, new ItemCreator(Material.DIAMOND_BOOTS, "§aVelocidade IV").build());

        inventory.setItem(22, new ItemCreator(Material.INK_SACK, (gamePlayer.isShowSpectators() ? "§a" : "§c") + "Espectadores").setId((gamePlayer.isShowSpectators() ? 10 : 8)).build());

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();
        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());

        if (inventory == null)return;
        if (itemStack == null)return;

        if (!inventory.getTitle().equalsIgnoreCase("Configurações"))return;

        event.setCancelled(true);

        if (event.getRawSlot() == 11) {
            gamePlayer.setSpeed(0);
            player.setFlySpeed(0.1f);
            player.getActivePotionEffects().forEach(potionEffect -> { if (potionEffect.getType().equals(PotionEffectType.SPEED)) { player.removePotionEffect(potionEffect.getType()); } });
            player.closeInventory();
        } else if (event.getRawSlot() == 12) {
            gamePlayer.setSpeed(1);
            player.setFlySpeed(0.2f);
            player.getActivePotionEffects().forEach(potionEffect -> { if (potionEffect.getType().equals(PotionEffectType.SPEED)) { player.removePotionEffect(potionEffect.getType()); } });
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
            player.closeInventory();
        } else if (event.getRawSlot() == 13) {
            gamePlayer.setSpeed(2);
            player.setFlySpeed(0.3f);
            player.getActivePotionEffects().forEach(potionEffect -> { if (potionEffect.getType().equals(PotionEffectType.SPEED)) { player.removePotionEffect(potionEffect.getType()); } });
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            player.closeInventory();
        } else if (event.getRawSlot() == 14) {
            gamePlayer.setSpeed(3);
            player.setFlySpeed(0.4f);
            player.getActivePotionEffects().forEach(potionEffect -> { if (potionEffect.getType().equals(PotionEffectType.SPEED)) { player.removePotionEffect(potionEffect.getType()); } });
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
            player.closeInventory();
        } else if (event.getRawSlot() == 15) {
            gamePlayer.setSpeed(4);
            player.setFlySpeed(1f);
            player.getActivePotionEffects().forEach(potionEffect -> { if (potionEffect.getType().equals(PotionEffectType.SPEED)) { player.removePotionEffect(potionEffect.getType()); } });
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
            player.closeInventory();
        } else if (event.getRawSlot() == 22) {
            gamePlayer.setShowSpectators(!gamePlayer.isShowSpectators());
            player.closeInventory();
            ConfigurationMenu.open(player);
        }
    }
}
