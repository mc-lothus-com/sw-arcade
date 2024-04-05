package com.lothus.skywars.arena.chests.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChestType {

    BASIC("Básico 01", "basic"),
    BASIC_2("Básico 02", "basic2"),

    MINI_FEAST("Mini Feast","minifeast"),
    FEAST("Feast", "feast"),

    REFIL("Refil", "refil");

    final String name;
    final String finder;

    public static ChestType getTagByName(String name) {
        for (ChestType tag : ChestType.values()) {
            if (tag.name().equalsIgnoreCase(name.toUpperCase())) {
                return tag;
            }
            if (tag.getName().equalsIgnoreCase(name.toLowerCase())) {
                return tag;
            }
        }
        return null;
    }


    public static boolean exists(String name) {
        for (ChestType tag : ChestType.values()) {
            if (tag.name().equalsIgnoreCase(name.toUpperCase()))
                return true;

            if (tag.getName().equalsIgnoreCase(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}