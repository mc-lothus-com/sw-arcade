package com.lothus.skywars.arena.team;

import com.lothus.core.games.room.RoomType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
public class ArenaTeam {

    private String[] teamName = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private String name;
    private List<UUID> players;
    private RoomType type;

    private Location spawn;

    public ArenaTeam(RoomType type, int pos) {
        this.type = type;
        this.name = teamName[pos];
        this.players = new ArrayList<>();
    }

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public boolean isPlayer(UUID player) {
        return players.contains(player);
    }

    public boolean isFull() {
        return players.size() >= type.getMaxPlayersPerTeam();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
}
