package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class InfectionGameUtils {

    static HashSet<UUID> playersInGame = new HashSet<>();
    static HashMap<UUID, Location>  playerPreviousLocations = new HashMap<>();


    public static void joinGame(Player p) {
        if (!playersInGame.contains(p.getUniqueId())) {
            p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
            playersInGame.add(p.getUniqueId());
            playerPreviousLocations.put(p.getUniqueId(), p.getLocation());
        }
        else {
            p.sendMessage(ChatColor.RED + "You are already in a game.");
        }
    }
    public static void leaveGame(Player p) {
        if (playersInGame.contains(p.getUniqueId())) {
            playersInGame.remove(p.getUniqueId());
            p.teleport(playerPreviousLocations.get(p.getUniqueId()));
            playerPreviousLocations.remove(p.getUniqueId());
        }
    }
}
