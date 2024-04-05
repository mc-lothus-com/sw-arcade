package com.lothus.skywars.cage;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.io.File;

@Getter @Setter
public class Cage {

    private String identify;

    private String name;
    private Material icon;

    private File schematic;

    public Cage(String identify, String name, Material icon) {
        this.identify = identify;
        this.name = name;
        this.icon = icon;
    }


}
