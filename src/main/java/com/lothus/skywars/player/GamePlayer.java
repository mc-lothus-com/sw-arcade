package com.lothus.skywars.player;

import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.lothus.skywars.arena.Arena;
import com.lothus.sync.stats.data.type.DataType;
import com.lothus.sync.stats.platform.Platform;
import com.lothus.sync.stats.player.games.skywars.SkyPlayer;
import com.lothus.sync.stats.player.games.skywars.stats.SkyStats;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

import static com.lothus.core.games.room.RoomType.SOLO;


@Getter @Setter
public class GamePlayer {

    private UUID uniqueId;

    private int kills;
    private int wins;
    private int lose;
    private int deaths;

    private boolean party;

    private boolean showSpectators;
    private int speed;

    public GamePlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;

        kills = 0;
        wins = 0;
        lose = 0;
        deaths = 0;

        showSpectators = true;
        speed = 0;
    }

    public void updateStats() {
        SkyPlayer skyPlayer = Platform.getSkyPlatform().getSkyPlayerController().getAccount(uniqueId);
        SkyStats stats = Platform.getSkyPlatform().getSkyPlayerController().get((com.lothus.skywars.platform.Platform.getMatch().getType() == SOLO ? DataType.SKY_WARS_SOLO : DataType.SKY_WARS_TEAM), uniqueId);

        if (stats != null) {
            GamePlayer gamePlayer = com.lothus.skywars.platform.Platform.getGamePlayerManager().get(uniqueId);

            stats.setKills(stats.getKills() + gamePlayer.getKills());
            if (gamePlayer.getDeaths() != 0) {
                stats.setDeaths(stats.getDeaths() + 1);
            }
            if (gamePlayer.getLose() != 0) {
                stats.setLoses(stats.getLoses() + gamePlayer.getLose());
            }
            stats.setGames(stats.getGames() + 1);
            skyPlayer.setCoins(skyPlayer.getCoins() + coin(gamePlayer.getKills()));
            if (gamePlayer.getWins() != 0) {
                stats.setWins(stats.getWins() + 1);
                skyPlayer.setXp(skyPlayer.getXp() + xp(gamePlayer.getKills() + 1));
                stats.setCurrentWinstreak(stats.getCurrentWinstreak() + 1);
                if (stats.getBestWinstreak() < stats.getCurrentWinstreak()) {
                    stats.setBestWinstreak(stats.getCurrentWinstreak());
                }
            } else {
                stats.setCurrentWinstreak(0);
            }

            Arena arena = com.lothus.skywars.platform.Platform.getMatch();
            if (!arena.getWinner().isPlayer(uniqueId)) {
                stats.setCurrentWinstreak(0);
            }


            SkyStats skyStats = Platform.getSkyPlatform().getSkyPlayerController().get(com.lothus.skywars.platform.Platform.getMatch().getType() == SOLO ? DataType.SKY_WARS_TEAM : DataType.SKY_WARS_SOLO, uniqueId);
            skyPlayer.setTotalKills(stats.getKills() + skyStats.getKills());
            skyPlayer.setTotalWins(stats.getWins() + skyStats.getWins());
            Platform.getDataPlayer().update(DataType.SKY_WARS_ACCOUNT, skyPlayer);
            Platform.getDataStats().update((com.lothus.skywars.platform.Platform.getMatch().getType() == SOLO ?
                    DataType.SKY_WARS_SOLO : DataType.SKY_WARS_TEAM), stats);
        }
    }

    public int coin(int k) {
        LothPlayer lothPlayer = Core.getDataPlayer().get(uniqueId);
        if (lothPlayer.getGroup().getRank() == Rank.VIP || lothPlayer.getGroup().getRank() == Rank.PRO) {
            return k*5;
        }

        if (lothPlayer.getGroup().getRank().ordinal() <= Rank.MASTER.ordinal()) {
            return k*7;
        }
        return k*3;
    }

    public int xp(int k) {
        LothPlayer lothPlayer = Core.getDataPlayer().get(uniqueId);
        if (lothPlayer.getGroup().getRank() == Rank.VIP || lothPlayer.getGroup().getRank() == Rank.PRO) {
            return k*4;
        }

        if (lothPlayer.getGroup().getRank().ordinal() <= Rank.MASTER.ordinal()) {
            return k*5;
        }
        return k*2;
    }

}
