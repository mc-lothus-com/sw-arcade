package com.lothus.skywars.scoreboard;

import com.lothus.core.api.scoreboard.TScoreboard;
import com.lothus.core.games.room.RoomType;
import com.lothus.core.games.state.GameState;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.sync.stats.data.type.DataType;
import com.lothus.sync.stats.player.games.skywars.stats.SkyStats;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArenaScoreboard extends TScoreboard {

    GamePlayer gamePlayer;
    Arena arena;

    public ArenaScoreboard(Player player) {
        super(player, "SKYWARS", "§2§lSKY WARS");
        this.gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
        this.arena = Platform.getMatch();
    }

    @Override
    public void create() {
        if (arena == null) return;

        SkyStats stats = Platform.getMatch().getType() == RoomType.SOLO ? com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId()) : com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
        if (arena.getState().equals(GameState.ESPERANDO)) {
            setRow(0, "");
            setRow(1, "§fPlayers: §a" + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
            setRow(2, "");
            setRow(3, "§fAguardando...");
            setRow(4, "");
            if (arena.getType().equals(RoomType.SOLO)) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
            } else if (arena.getType().equals(RoomType.DUPLAS)) {
                SkyStats team = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
                setRow(5, "§fKit: §a" + team.getKit().replace("None", "Nenhum"));
            }
            setRow(6, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
            setRow(7, "");
            setRow(8, "§fMapa: §a" + arena.getName());
            setRow(9, "§fModo: §7" + arena.getType().getName());
            setRow(10, "");
            setRow(11, "§awww.mc-lothus.com");
        }

        if (arena.getState().equals(GameState.INICIANDO) || arena.getState().equals(GameState.PREPARANDO)) {
            setRow(0, "");
            setRow(1, "§fPlayers: §a" + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
            setRow(2, "");
            setRow(3, "§fInicia em: §a" + arena.getTask().getTime() + "s");
            setRow(4, "");
            if (arena.getType().equals(RoomType.SOLO)) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
            } else if (arena.getType().equals(RoomType.DUPLAS)) {
                SkyStats team = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
                setRow(5, "§fKit: §a" + team.getKit().replace("None", "Nenhum"));
            }
            setRow(6, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
            setRow(7, "");
            setRow(8, "§fMapa: §a" + arena.getName());
            setRow(9, "§fModo: §7" + arena.getType().getName());
            setRow(10, "");
            setRow(11, "§awww.mc-lothus.com");
        }


        if (arena.getState().equals(GameState.EM_JOGO) || arena.getState() == GameState.ENCERRANDO) {
            if (arena.getType() == RoomType.SOLO) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(0, "");
                setRow(1, "§fTempo: §a" + secondToMinutes((int) arena.getTask().getTime()));
                setRow(2, "");
                setRow(3, "§fRestantes: §a" + arena.getPlayers().size());
                setRow(4, "");
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
                setRow(6, "§fKills: §a" + gamePlayer.getKills());
                setRow(7, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
                setRow(8, "");
                setRow(9, "§fMapa: §a" + arena.getName());
                setRow(10, "§fModo: §7" + arena.getType().getName());
                setRow(11, "");
                setRow(12, "§awww.mc-lothus.com");
            } else {
                setRow(0, "");
                setRow(1, "§fTempo: §a" + secondToMinutes((int) arena.getTask().getTime()));
                setRow(2, "");
                setRow(3, "§fRestantes: §a" + arena.getPlayers().size());
                setRow(4, "§fTimes Restantes: §a" + arena.getTeams().size());
                setRow(5, "");
                setRow(6, "§fKit: §a" + stats.getKit().replace("None", "Nenhum"));
                setRow(7, "§fKills: §a" + gamePlayer.getKills());
                setRow(8, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
                setRow(9, "");
                setRow(10, "§fMapa: §a" + arena.getName());
                setRow(11, "§fModo: §7" + arena.getType().getName());
                setRow(12,"");
                setRow(13, "§awww.mc-lothus.com");
            }
        }
    }

    @Override
    public void update() {
        if (arena == null) return;
        SkyStats stats = Platform.getMatch().getType() == RoomType.SOLO ? com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId()) : com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
        if (arena.getState().equals(GameState.ESPERANDO)) {
            setRow(0, "");
            setRow(1, "§fPlayers: §a" + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
            setRow(2, "");
            setRow(3, "§fAguardando...");
            setRow(4, "");
            if (arena.getType().equals(RoomType.SOLO)) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
            } else if (arena.getType().equals(RoomType.DUPLAS)) {
                SkyStats team = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
                setRow(5, "§fKit: §a" + team.getKit().replace("None", "Nenhum"));
            }
            setRow(6, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
            setRow(7, "");
            setRow(8, "§fMapa: §a" + arena.getName());
            setRow(9, "§fModo: §7" + arena.getType().getName());
            setRow(10, "");
            setRow(11, "§awww.mc-lothus.com");
        }

        if (arena.getState().equals(GameState.INICIANDO) || arena.getState().equals(GameState.PREPARANDO)) {
            setRow(0, "");
            setRow(1, "§fPlayers: §a" + arena.getPlayers().size() + "/" + arena.getMaxPlayers());
            setRow(2, "");
            setRow(3, "§fInicia em: §a" + arena.getTask().getTime() + "s");
            setRow(4, "");
            if (arena.getType().equals(RoomType.SOLO)) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
            } else if (arena.getType().equals(RoomType.DUPLAS)) {
                SkyStats team = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_TEAM, player.getUniqueId());
                setRow(5, "§fKit: §a" + team.getKit().replace("None", "Nenhum"));
            }
            setRow(6, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
            setRow(7, "");
            setRow(8, "§fMapa: §a" + arena.getName());
            setRow(9, "§fModo: §7" + arena.getType().getName());
            setRow(10, "");
            setRow(11, "§awww.mc-lothus.com");
        }


        if (arena.getState().equals(GameState.EM_JOGO) || arena.getState() == GameState.ENCERRANDO) {
            if (arena.getType() == RoomType.SOLO) {
                SkyStats solo = com.lothus.sync.stats.platform.Platform.getDataStats().getSkyStats(DataType.SKY_WARS_SOLO, player.getUniqueId());
                setRow(0, "");
                setRow(1, "§fTempo: §a" + secondToMinutes((int) arena.getTask().getTime()));
                setRow(2, "");
                setRow(3, "§fRestantes: §a" + arena.getPlayers().size());
                setRow(4, "");
                setRow(5, "§fKit: §a" + solo.getKit().replace("None", "Nenhum"));
                setRow(6, "§fKills: §a" + gamePlayer.getKills());
                setRow(7, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
                setRow(8, "");
                setRow(9, "§fMapa: §a" + arena.getName());
                setRow(10, "§fModo: §7" + arena.getType().getName());
                setRow(11, "");
                setRow(12, "§awww.mc-lothus.com");
            } else {
                setRow(0, "");
                setRow(1, "§fTempo: §a" + secondToMinutes((int) arena.getTask().getTime()));
                setRow(2, "");
                setRow(3, "§fRestantes: §a" + arena.getPlayers().size());
                setRow(4, "§fTimes Restantes: §a" + arena.getTeams().size());
                setRow(5, "");
                setRow(6, "§fKit: §a" + stats.getKit().replace("None", "Nenhum"));
                setRow(7, "§fKills: §a" + gamePlayer.getKills());
                setRow(8, "§fWinstreak: §a" + (stats == null ? "0" : stats.getCurrentWinstreak()));
                setRow(9, "");
                setRow(10, "§fMapa: §a" + arena.getName());
                setRow(11, "§fModo: §7" + arena.getType().getName());
                setRow(12,"");
                setRow(13, "§awww.mc-lothus.com");
            }
        }

    }

    public int getTeams() {
        int i = 0;
        Arena a = Platform.getMatch();

        for (ArenaTeam team : a.getTeams()) {
            Player p = Bukkit.getPlayer(team.getPlayers().get(0));
            Player p2 = null;


            if (team.getPlayers().size() >= 2) {
                p2 = Bukkit.getPlayer(team.getPlayers().get(1));
                if (!a.isPlayer(p2) && !a.isPlayer(p)) {
                    continue;
                }

                if (a.isPlayer(p) || a.isPlayer(p2)) {
                    i++;
                    continue;
                }
            }

            if (a.isPlayer(p)) {
                i++;
            }
        }
        return i;
    }

    public static String secondToMinutes(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        String formato = "%02d:%02d";
        return String.format(formato, minutos, segundosRestantes);
    }
}
