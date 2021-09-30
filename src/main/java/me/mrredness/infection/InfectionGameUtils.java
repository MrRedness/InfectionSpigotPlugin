package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class InfectionGameUtils {

    static HashSet<Player> playersInGame = new HashSet<>();
    static HashMap<UUID, Location>  playerPreviousLocations = new HashMap<>();
    static boolean lobbyStage = true;
    static BukkitTask barCountdown;


    public static void joinGame(Player p, boolean worldBorderEnabled, Infection plugin) {
        if (!playersInGame.contains(p)) {
            p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
            playersInGame.add(p);
            playerPreviousLocations.put(p.getUniqueId(), p.getLocation());
            p.sendMessage(ChatColor.GREEN + "Welcome to Infection!");
            int numberOfMorePlayersNeeded = (Integer) DataHelper.get("Min Number of Players") - playersInGame.size();
            if (numberOfMorePlayersNeeded == 1) {
                for (Player players : playersInGame) {
                    players.sendMessage(ChatColor.AQUA + randomJoinMessage(p));
                    players.sendMessage(ChatColor.GOLD + "We now need " + ChatColor.BLUE + 1 + ChatColor.GOLD + " more player.");
                }
            } else {
                for (Player players : playersInGame) {
                    players.sendMessage(ChatColor.GOLD + "We now need " + ChatColor.BLUE + numberOfMorePlayersNeeded + ChatColor.GOLD + " more players.");
                }
            }
            if (playersInGame.size() == 1) {
                if (worldBorderEnabled) {
                    BorderUtils.setBorder("Infection Lobby Border Range", "Infection Lobby World");
                }
                barCountdown= new BarCountdown(playersInGame, lobbyStage).runTaskAsynchronously(plugin);
            }
        }
        else {
            p.sendMessage(ChatColor.RED + "You are already in a game.");
        }
    }
    public static void leaveGame(Player p) {
        if (playersInGame.contains(p)) {
            playersInGame.remove(p);
            p.teleport(playerPreviousLocations.get(p.getUniqueId()));
            playerPreviousLocations.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GREEN + "Bye!");
            if (playersInGame.size() == 0) {
                BorderUtils.removeBorder("Infection Lobby Border Range", "Infection Lobby World");
                barCountdown.cancel();
            }
        }
        else {
            p.sendMessage(ChatColor.RED + "You are not currently in a game");
        }
    }
    public static String randomJoinMessage(Player p) {
        Random random = new Random();
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {return "How nice of you to join us, " + p.getDisplayName() + ".";}
            else {return "We have been graced by the presence of " + p.getDisplayName() + ".";}
        } else {
            if (random.nextBoolean()) {return p.getDisplayName() + " slid into your DMs!";}
            else {return "Yay, " + p.getDisplayName() + " made it!";}
        }
    }
    /*
    public static void barCountdown() {
        BossBar countdownBar = Bukkit.createBossBar("Waiting for Players", BarColor.YELLOW, BarStyle.SEGMENTED_10);
        while (playersInGame.size() > 0 && lobbyStage) {
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            int numberOfMorePlayersNeeded = (Integer) DataHelper.get("Min Number of Players") - playersInGame.size();
            if (numberOfMorePlayersNeeded == 1) {
                countdownBar.setTitle(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + 1 + ChatColor.GOLD + " more player.");
            } else {
                countdownBar.setTitle(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + numberOfMorePlayersNeeded + ChatColor.GOLD + " more players.");
            }
            SleepUtils.three();
        }
    }

    private static void messagesCountdown() {
        while (playersInGame.size() > 0 && lobbyStage) {
            int numberOfMorePlayersNeeded = (Integer) DataHelper.get("Min Number of Players") - playersInGame.size();
            if (numberOfMorePlayersNeeded == 1) {
                for (Player p : playersInGame) {
                    p.sendMessage(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + 1 + ChatColor.GOLD + " more player.");
                }
            } else {
                for (Player p : playersInGame) {
                    p.sendMessage(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + numberOfMorePlayersNeeded + ChatColor.GOLD + " more players.");
                }
            }
            SleepUtils.ten();
        }
    }

     */
}
