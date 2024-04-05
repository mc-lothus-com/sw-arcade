package com.lothus.skywars.menus;

import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.utils.bukkit.ItemCreator;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CompassMenu implements Listener {

    public static void open(Player player) {
        Arena a = Platform.getMatch();
        int p = a.getPlayers().size();
        Inventory inventory = Bukkit.createInventory(null, (p < 9 ? 9 : 9*2) , "Bússola");

        if (a == null) {
            return;
        }

        int slot = -1;
        for (Player players : a.getPlayers()) {
            slot++;

            int health = (int) players.getHealth();
            LothPlayer lothPlayer = Core.getPlayerController().get(players.getUniqueId());
            inventory.setItem(slot, new ItemCreator(
                    Material.SKULL_ITEM,
                    (lothPlayer.getSocial().getFake().getName() == lothPlayer.getName() ? lothPlayer.getGroup().getRank().getColor() + lothPlayer.getName() : lothPlayer.getSocial().getFake().getRank().getColor() + lothPlayer.getSocial().getFake().getName())
            ).withSkullOwner(players.getName())
                    .setLore(
                    "§fVida: §c" + health + "♥",
                    "§eClique para teleportar."
                    )
                    .setId(3).setAmount(1).build());
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getClickedInventory();
        ItemStack itemStack = event.getCurrentItem();

        if (inventory == null) return;
        if (itemStack == null) return;
        if (!inventory.getName().equalsIgnoreCase("Bússola")) return;

        Arena a = Platform.getMatch();

        if (a == null)return;

        event.setCancelled(true);

        if (itemStack.getType() == Material.AIR)return;

        for (Player players : a.getPlayers()) {
            if (itemStack.getItemMeta().getDisplayName().endsWith(players.getName())) {
                player.teleport(players);
                player.sendMessage("§aVocê foi teleportado para " + itemStack.getItemMeta().getDisplayName() + "§a.");
                player.closeInventory();
                return;
            }
        }
    }


}
