package com.lothus.skywars.commands;

import com.lothus.bukkit.commands.CommandBase;
import com.lothus.core.Core;
import com.lothus.core.games.room.RoomType;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.lothus.skywars.arena.creator.ArenaCreator;
import com.lothus.skywars.platform.Platform;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MapCommand extends CommandBase {

    public MapCommand() {
        super("map");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());

        if (lothPlayer == null) {
            return true;
        }

        if (lothPlayer.getGroup().getRank() != Rank.CEO) {
            if (!lothPlayer.getGroup().containsPermission("skywars.map")) {
                player.sendMessage("§cVocê não tem permissão para executar este comando.");
                return true;
            }
        }

        if (args.length == 0) {
            player.sendMessage("§cSintaxe incorreta, utilize '/map [name] [type]'.");
            return true;
        }

        if (args.length > 0) {
            String arenaName = args[0];
            if (args.length > 1) {
                String id = args[1];
                if (args.length > 2) {
                    RoomType arenaType = RoomType.getRoomType(args[2]);

                    if (arenaType == null) {
                        player.sendMessage("§cTipo de mapa inválido.");
                        return true;
                    }

                    ArenaCreator arenaCreator = new ArenaCreator(player);
                    arenaCreator.setId(id);
                    arenaCreator.setName(arenaName);
                    arenaCreator.setType(arenaType);
                    arenaCreator.sendItems();
                    Platform.getArenaCreatorManager().load(arenaCreator);
                    player.sendMessage("");
                    player.sendMessage("§6§lMODO CRIADOR:");
                    player.sendMessage("§eVocê iniciou a criação da arena §6" + arenaName + "§e.");
                    player.sendMessage("§eDetermine as localizações e salve a arena.");
                    player.sendMessage("");
                    return true;
                }
            }

            player.sendMessage("§cVocê deve inserir o tipo de arena para continuar.");
            return true;
        }

        return false;
    }
}
