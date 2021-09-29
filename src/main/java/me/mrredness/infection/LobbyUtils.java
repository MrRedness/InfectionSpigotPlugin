package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;

public class LobbyUtils {

    static HashSet<UUID> playersInLobby = new HashSet<>();

    static void tpToLobby(Player p) {
        p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
    }
    public static void joinLobby(Player p) {

        tpToLobby(p);
        playersInLobby.add(p.getUniqueId());
    }
}
