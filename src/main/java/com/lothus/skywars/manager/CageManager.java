package com.lothus.skywars.manager;

import com.lothus.skywars.cage.Cage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CageManager {

    private final HashMap<String, Cage> creators = new HashMap<>();

    public void load(Cage creator) {
        creators.put(creator.getIdentify(), creator);
    }

    public void unload(String uniqueId) {
        creators.remove(uniqueId);
    }

    public Cage get(String uniqueId) {
        return creators.get(uniqueId);
    }

    public List<Cage> getAll() {
        return new ArrayList<>(creators.values());
    }
}
