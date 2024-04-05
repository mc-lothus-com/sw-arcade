package com.lothus.skywars.utils;

import com.lothus.core.Core;
import com.lothus.core.servers.ServerInfo;
import com.lothus.core.servers.type.ServerType;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class PlayerUtil {

    public static void send(Player player, ServerType type) {
        ServerInfo serverInfo = getServerInfo(type);

        if (serverInfo == null) {
            player.sendMessage("§cNão foi possível estabelecer conexão com o lobby.");
            return;
        }

        com.lothus.core.utils.bukkit.player.PlayerUtil.connect(player.getUniqueId(), serverInfo);
    }

    private static ServerInfo getServerInfo(ServerType type) {
        Comparator<ServerInfo> comparator = Comparator.comparing(ServerInfo::getPlayers);
        List<ServerInfo> list = Core.getServerController().get(type);
        list.sort(comparator);

        if (list.isEmpty()) {
            return null;
        }

        return list.get(0);
    }
}
