package com.lothus.skywars.commands;

import com.lothus.bukkit.commands.CommandBase;
import com.lothus.core.Core;
import com.lothus.core.player.LothPlayer;
import com.lothus.core.player.group.rank.Rank;
import com.lothus.skywars.arena.Arena;
import com.lothus.skywars.platform.Platform;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand extends CommandBase {

    public TimeCommand() {
        super("time", "tempo", "tempo");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        LothPlayer lothPlayer = Core.getPlayerController().get(player.getUniqueId());

        if (lothPlayer.getGroup().getRank() != Rank.CEO) {
            if (!lothPlayer.getGroup().containsPermission("skywars.time")) {
                player.sendMessage("§cVocê não tem permissão para executar este comando.");
                return true;
            }
        }

        Arena arena = Platform.getMatch();

        if (args.length == 0) {
            player.sendMessage("§cSintaxe incorreta, utilize '/tempo [tempo]'.");
            return true;
        }

        if (args.length > 0) {
            if (isInteger(args[0])) {
                long time = Long.parseLong(args[0]);

                if (time < 6) {
                    player.sendMessage("§cO tempo deve ser maior que 6.");
                    return true;
                }

                arena.getTask().setTime(time);
                player.sendMessage("§6O temporizador foi alterado para " + time + " segundos.");
            }
        }
        return false;
    }
}
