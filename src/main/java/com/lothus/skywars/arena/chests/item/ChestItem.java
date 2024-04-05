package com.lothus.skywars.arena.chests.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter @Setter
@AllArgsConstructor
public class ChestItem {

    private ItemStack itemStack;
    private int percent;
}
