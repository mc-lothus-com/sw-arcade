package com.lothus.skywars;

import com.lothus.bukkit.commands.loader.BukkitCommandLoader;
import com.lothus.core.Core;
import com.lothus.core.api.loaders.ListenerLoader;
import com.lothus.core.games.GameInfo;
import com.lothus.core.games.type.GameType;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.cage.Cage;
import com.lothus.skywars.platform.Platform;
import com.lothus.sync.stats.Sync;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;

public class Instance extends JavaPlugin {

    @Getter @Setter
    private static Instance instance;

    @Override
    public void onLoad() {
        setInstance(this);
        File file = new File(getDataFolder().getPath());

        if (file.exists()) {
            saveDefaultConfig();
            paste("world", "arena");
        }
    }

    @Override
    public void onEnable() {
        loadArena();
        worldConfig();
        loadCages();
        new Sync(this, GameType.SKY_WARS);
        ListenerLoader.loadListeners(this, "com.lothus.engines.skywars.menus");
        BukkitCommandLoader.loadCommands(this, "com.lothus.engines.skywars.commands");
        ListenerLoader.loadListeners(this, "com.lothus.engines.skywars.com.redelegit.npc.listener");
    }

    @Override
    public void onDisable() {
        Platform.getMatch().stop();
    }

    public void loadCages() {
        File file = new File(getDataFolder(), "cages.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
                InputStream inputStream = getResource(file.getName());
                YamlConfiguration configuration = YamlConfiguration.loadConfiguration(inputStream);
                configuration.save(file);
            } catch (Exception ignore) {}
        } else {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            for (String c : configuration.getConfigurationSection("cages").getKeys(false)) {
                Cage cage = new Cage(
                        c,
                        configuration.getString("cages." + c + ".name"),
                        Material.getMaterial(
                                configuration.getString("cages." + c + ".icon")
                        )
                );

                File schem = new File(getDataFolder().getPath() + "/cages", configuration.getString("cages." + c + ".schematicName"));
                if (!schem.exists())continue;

                cage.setSchematic(schem);
                Platform.getCageManager().load(cage);
                Core.getLogger().info("A cage " + cage.getName() + " foi carregada com sucesso.");
            }
        }
    }

    public void loadArena() {
        File file = new File(getDataFolder(), "arena.yml");
        if (file.exists()) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            try {
                Arena arena = new Arena(configuration);
                Platform.setMatch(arena);
                getServer().getScheduler().runTaskLater(this, () -> {
                    arena.setGameInfo(new GameInfo(arena.getId(), arena.getName(), Core.getServerInfo().getName(), GameType.SKY_WARS, arena.getType(), arena.getMaxPlayers()));
                    arena.start();
                    Core.getRedis().message("GAME_START", Core.getGson().toJson(arena.getGameInfo()));
                }, 80L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void worldConfig() {
        Arena a = Platform.getMatch();
        if (a == null) {
            return;
        }

        Platform.getChestManager().loadItems();

        World world = Bukkit.getWorld("world");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doWeatherCycle", "false");
        world.setGameRuleValue("randomTickSpeed", "0");
        world.setAnimalSpawnLimit(1);
        world.setMonsterSpawnLimit(1);
        world.setDifficulty(Difficulty.NORMAL);

        world.setTime(0);

        world.setAutoSave(false);

        world.getEntities().forEach(Entity::remove);

        WorldBorder border = world.getWorldBorder();

        border.setCenter(a.getLobby());
        border.setSize(a.getBorderSize());
        border.setDamageAmount(1.0D);
    }

    public void paste(String world, String folder) {
        try {
            File dir = new File(Instance.getInstance().getDataFolder().getPath() + "/"+folder+"/");
            File to = new File("/home/container/" + world);

            FileUtils.deleteDirectory(to);

            if (!to.exists()) {
                to.mkdirs();
            }
            FileUtils.copyDirectory(dir, to);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
