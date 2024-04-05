package com.lothus.skywars.arena.chests;

import com.lothus.skywars.arena.chests.item.ChestItem;
import com.lothus.skywars.arena.chests.type.ChestType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;

@Getter @Setter
public class ChestInfo {

    private ChestType type;
    private Location location;
    private List<ChestItem> items;

    public ChestInfo(ChestType type) {
        setType(type);
    }

}