package com.lothus.skywars.arena;

import com.lothus.core.Core;
import com.lothus.core.api.tag.TagManager;
import com.lothus.core.games.GameInfo;
import com.lothus.core.games.room.RoomType;
import com.lothus.core.games.state.GameState;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.storage.redis.channels.RedisChannel;
import com.lothus.core.utils.bukkit.ItemCreator;
import com.lothus.skywars.Instance;
import com.lothus.skywars.arena.chests.type.ChestType;
import com.lothus.skywars.arena.cube.ArenaCube;
import com.lothus.skywars.arena.tasks.ArenaTask;
import com.lothus.skywars.arena.tasks.solo.ArenaSoloTask;
import com.lothus.skywars.arena.tasks.team.ArenaTeamTask;
import com.lothus.skywars.arena.team.ArenaTeam;
import com.lothus.skywars.platform.Platform;
import com.lothus.skywars.player.GamePlayer;
import com.lothus.skywars.utils.LocationUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.lothus.core.games.room.RoomType.DUPLAS;
import static com.lothus.core.games.room.RoomType.SOLO;
import static com.lothus.core.games.state.GameState.EM_JOGO;
import static com.lothus.core.games.state.GameState.ESPERANDO;
import static org.bukkit.Material.AIR;

@Getter @Setter
public class Arena {

    private String id;
    private String name;

    private Location lobby;

    private ArenaCube cube;
    private ArenaCube cubeLobby;

    private World world;

    private GameInfo gameInfo;

    private RoomType type;
    private GameState state;

    private int maxPlayers;
    private int minPlayers;

    private int borderSize;

    private int borderFirstSize;
    private int borderSecondSize;
    private int borderLastSize;

    private long time;

    private ArenaTeam winner;

    private ArenaTask task;

    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();

    private List<ArenaTeam> teams = new ArrayList<>();
    private List<Location> spawns = new ArrayList<>();

    private List<Location> chestBasic = new ArrayList<>();
    private List<Location> chestBasic2 = new ArrayList<>();
    private List<Location> chestMiniFeast = new ArrayList<>();;
    private List<Location> chestFeast = new ArrayList<>();;
    private List<Location> chestRefil = new ArrayList<>();

    private HashMap<Location, ChestType> chests = new HashMap<>();

    public Arena(String name, Location lobby) {
        this.name = name;
        this.lobby = lobby;

        setState(ESPERANDO);
    }

    public Arena(YamlConfiguration configuration) throws IOException {
        id = configuration.getString("id");
        name = configuration.getString("name");
        lobby = LocationUtils.getLocation(configuration.getString("lobby"));
        type = RoomType.getRoomType(configuration.getString("type"));
        minPlayers = configuration.getInt("minPlayers");
        maxPlayers = configuration.getInt("maxPlayers");

        borderSize = configuration.getInt("borderSize");
        borderFirstSize = configuration.getInt("borderFirstSize");
        borderSecondSize = configuration.getInt("borderSecondSize");
        borderLastSize = configuration.getInt("borderLastSize");

        Location cube1 = LocationUtils.getLocation(configuration.getString("cube.1"));
        Location cube2 = LocationUtils.getLocation(configuration.getString("cube.2"));

        Location lobbyCube1 = LocationUtils.getLocation(configuration.getString("lobbyCube.1"));
        Location lobbyCube2 = LocationUtils.getLocation(configuration.getString("lobbyCube.2"));

        cube = new ArenaCube(cube1, cube2);
        cubeLobby = new ArenaCube(lobbyCube1, lobbyCube2);

        List<Location> s = new ArrayList<>();
        List<Location> basic = new ArrayList<>();
        List<Location> basic2 = new ArrayList<>();
        List<Location> miniFeast = new ArrayList<>();
        List<Location> feast = new ArrayList<>();
        List<Location> refil = new ArrayList<>();


        configuration.getStringList("chests.basic").forEach(b -> basic.add(LocationUtils.getLocation(b)));
        configuration.getStringList("chests.basic2").forEach(b -> basic2.add(LocationUtils.getLocation(b)));
        configuration.getStringList("chests.minifeast").forEach(b -> miniFeast.add(LocationUtils.getLocation(b)));
        configuration.getStringList("chests.feast").forEach(b -> feast.add(LocationUtils.getLocation(b)));
        configuration.getStringList("chests.refil").forEach(b -> refil.add(LocationUtils.getLocation(b)));

        configuration.getStringList("spawns").forEach(spawn -> {
            Location location = LocationUtils.getLocation(spawn);
            s.add(location);
        });

        chestBasic = basic;
        chestBasic2 = basic2;
        chestMiniFeast = miniFeast;
        chestFeast = feast;
        chestRefil = refil;

        spawns = s;
        setState(ESPERANDO);
    }

    public void addPlayer(Player player) {
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());

        if (!players.contains(player)) {
            players.add(player);
        }

        spectators.remove(player);
        player.getInventory().clear();

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setExp(0);
        player.setLevel(0);
        player.setSaturation(10f);
        player.setGameMode(GameMode.SURVIVAL);

        player.teleport(lobby.add(0, 1.0, 0));
        lobby.subtract(0, 1.0, 0);

        player.setAllowFlight(false);
        player.setFlying(false);

        spectators.forEach(player::hidePlayer);

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        player.getInventory().setItem(0, new ItemCreator(Material.CHEST, "§aKits").build());
        player.getInventory().setItem(1, new ItemCreator(Material.NETHER_STAR, "§aHabilidade").build());
        player.getInventory().setItem(8, new ItemCreator(Material.BED, "§cSair").build());

        players.forEach(p -> {
            LothPlayer lp = Core.getPlayerController().get(p.getUniqueId());
            if (lp.getPrefs().isVanish()) {
                player.hidePlayer(p);
            }
            p.sendMessage((lothPlayer.getSocial().getFake().getName().equalsIgnoreCase(lothPlayer.getName()) ? lothPlayer.getGroup().getTag().getColor() + player.getName() : lothPlayer.getSocial().getFake().getRank().getColor() + lothPlayer.getSocial().getFake().getName())+ " §eentrou na partida (§b" + players.size() + "§e/§b" + maxPlayers + "§e).");
        });
    }
    public void removePlayer(Player player) {
        players.remove(player);

        player.getInventory().clear();

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setSaturation(10f);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(false);
        player.setFlying(false);

        if (state != EM_JOGO) {
            if (players.isEmpty())return;

            players.forEach(p -> {
                p.sendMessage("§b" + player.getName() + " §esaiu da partida (§b" + players.size() + "§e/§b" + maxPlayers + "§e).");
            });
        }
    }

    public void addSpectator(Player player) {
        GamePlayer gamePlayer = Platform.getGamePlayerManager().get(player.getUniqueId());
        if (!spectators.contains(player)) {
            spectators.add(player);
        }

        players.remove(player);
        player.getInventory().clear();

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setSaturation(10f);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(true);
        player.setFlying(true);

        players.forEach(p -> {
            p.hidePlayer(player);
        });

        spectators.forEach(p -> {
            if (gamePlayer.isShowSpectators()) {
                player.showPlayer(p);
            } else {
                player.hidePlayer(p);
            }
            p.showPlayer(player);
        });

        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));

        player.getInventory().setItem(0, new ItemCreator(Material.COMPASS, "§aLocalizar jogadores").build());
        player.getInventory().setItem(4, new ItemCreator(Material.REDSTONE_COMPARATOR, "§aConfigurações").build());
        player.getInventory().setItem(7, new ItemCreator(Material.PAPER, "§aJogar Novamente").build());
        player.getInventory().setItem(8, new ItemCreator(Material.BED, "§cSair").build());
    }

    public void removeSpectator(Player player) {
        spectators.remove(player);

        player.getInventory().clear();

        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setFallDistance(0);
        player.setSaturation(10f);
        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.ADVENTURE);

        player.setAllowFlight(false);
        player.setFlying(false);
    }

    public boolean isPlayer(Player player) {
        return players.contains(player);
    }
    public boolean isSpectator(Player player) {
        return spectators.contains(player);
    }

    public void start() {
        Arena a = this;
        if (type == SOLO) {
            task = new ArenaSoloTask(a);
        } else if (type == DUPLAS) {
            task = new ArenaTeamTask(a);
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                task.runTaskTimer(Instance.getInstance(), 0L, 20L);
            }
        });
        thread.start();
    }

    public ArenaTeam getTeam(Player player) {
        for (ArenaTeam team : teams) {
            if (team.isPlayer(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public void updateGameInfo() {
        gameInfo.setPlayers(players.size());
        gameInfo.setMaxPlayers(maxPlayers);
        gameInfo.setState(state);
        Core.getRedis().message("GAME_UPDATE", Core.getGson().toJson(gameInfo));
    }

    public void stop() {
        Core.getRedis().message(RedisChannel.GAME_STOP.name(), Core.getGson().toJson(gameInfo));
    }

    public void createArenaConfiguration() {
        File file = new File(Instance.getInstance().getDataFolder().getPath(), "arena.yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        List<String> s = new ArrayList<>();
        List<String> basic = new ArrayList<>();
        List<String> basic2 = new ArrayList<>();
        List<String> miniFeast = new ArrayList<>();
        List<String> feast = new ArrayList<>();
        List<String> refil = new ArrayList<>();

        configuration.set("id", id);
        configuration.set("name", name);
        configuration.set("type", type.name());
        configuration.set("lobby", LocationUtils.getData(lobby));
        configuration.set("maxPlayers", maxPlayers);
        configuration.set("minPlayers", minPlayers);

        configuration.set("cube.1", LocationUtils.getData(cube.getMaxPoint()));
        configuration.set("cube.2", LocationUtils.getData(cube.getMinPoint()));

        configuration.set("lobbyCube.1", LocationUtils.getData(cubeLobby.getMaxPoint()));
        configuration.set("lobbyCube.2", LocationUtils.getData(cubeLobby.getMinPoint()));

        configuration.set("borderSize", 200);
        configuration.set("borderFirstSize", 200);
        configuration.set("borderSecondSize", 200);
        configuration.set("borderLastSize", 200);

        spawns.forEach(location -> s.add(LocationUtils.getData(location)));

        chestBasic.forEach(location -> basic.add(LocationUtils.getData(location)));
        chestBasic2.forEach(location -> basic2.add(LocationUtils.getData(location)));
        chestMiniFeast.forEach(location -> miniFeast.add(LocationUtils.getData(location)));
        chestFeast.forEach(location -> feast.add(LocationUtils.getData(location)));
        chestRefil.forEach(location -> refil.add(LocationUtils.getData(location)));

        configuration.set("spawns", s);
        configuration.set("chests.basic", basic);
        configuration.set("chests.basic2", basic2);
        configuration.set("chests.minifeast", miniFeast);
        configuration.set("chests.feast", feast);
        configuration.set("chests.refil", refil);

        try {
            configuration.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBorder(int size) {
        World world = lobby.getWorld();
        WorldBorder border = world.getWorldBorder();
        border.setSize(size);
    }

    public void updateTags() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isSpectator(player)) {
                TagManager.setTag(player, "§8", "", "Z");
            } else if (isPlayer(player)) {
                ArenaTeam team = getTeam(player);
                if (team == null) return;
                TagManager.setTag(player, "§c[" + getTeam(player).getName() + "] ", "", getTeam(player).getName());
            }
        }
    }

    public void createTeams() {
        int numPlayers = getPlayers().size();
        int numTeams = (int) Math.ceil(numPlayers / 2.0);

        if (type == SOLO) {
            int i = 0;
            for (Player player : getPlayers()) {
                ArenaTeam team = new ArenaTeam(type, i);

                team.setSpawn(getSpawns().get(i));
                team.addPlayer(player.getUniqueId());

                teams.add(team);

                Core.getLogger().info(team.getSpawn().toString());
                i++;
            }
        }else if (type == DUPLAS) {
            for (int i = 0; i < numTeams; i++) {
                ArenaTeam team = new ArenaTeam(type, i);

                Player p1 = getPlayers().get(i * 2);
                Player p2 = null;

                if ((i * 2) + 1 < numPlayers) {
                    p2 = getPlayers().get((i * 2) + 1);
                }

                team.setSpawn(getSpawns().get(i));
                team.addPlayer(p1.getUniqueId());

                if (p2 != null) {
                    team.addPlayer(p2.getUniqueId());
                    Core.getLogger().info(p1.getName() + " and " + p2.getName());
                } else {
                    Core.getLogger().info(p1.getName() + " (no partner)");
                }
                teams.add(team);
            }
        }
    }

    public void removeCages() {
        spawns.forEach(s -> {
            getBlocksAroundLocation(s, 3).forEach(block -> {
                block.setType(AIR);
            });
        });
    }

    public List<Block> getBlocksAroundLocation(Location location, int radius) {
        World world = location.getWorld();
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();

        List<Block> blocks = new ArrayList<>();

        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    Block block = world.getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public ArenaTeam findTeamByCompass(Player author) {
        List<Location> ps = Arrays.asList(players.stream().map(Player::getLocation).toArray(Location[]::new));
        ps = new ArrayList<>(ps);

        ps.remove(author.getLocation());

        Location location = author.getLocation().clone();
        Comparator<Location> comparator = Comparator.comparing(location::distance);

        ps.sort(comparator);

        if (ps.isEmpty()) {
            return null;
        }

        List<Location> finalPs = ps;
        return players.stream().filter(player -> player.getLocation().equals(finalPs.get(0))).map(this::getTeam).findFirst().orElse(null);
    }

    private void b(Location location, Material material) {
        location.getBlock().getRelative(BlockFace.DOWN).setType(material);
        location.getBlock().getRelative(BlockFace.EAST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH).setType(material);
        location.getBlock().getRelative(BlockFace.UP).setType(material);
        location.getBlock().getRelative(BlockFace.WEST).setType(material);
    }

    private void a(Location location, Material material) {
        location.getBlock().getRelative(BlockFace.DOWN).setType(material);
        location.getBlock().getRelative(BlockFace.EAST).setType(material);
        location.getBlock().getRelative(BlockFace.EAST_NORTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.EAST_SOUTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH).setType(material);
        location.getBlock().getRelative(BlockFace.UP).setType(material);
        location.getBlock().getRelative(BlockFace.WEST).setType(material);
        location.getBlock().getRelative(BlockFace.WEST_NORTH_WEST).setType(material);
        location.getBlock().getRelative(BlockFace.WEST_SOUTH_WEST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH_NORTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH_NORTH_WEST).setType(material);
        location.getBlock().getRelative(BlockFace.NORTH_WEST).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH_WEST).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH_SOUTH_EAST).setType(material);
        location.getBlock().getRelative(BlockFace.SOUTH_SOUTH_WEST).setType(material);
    }

}
