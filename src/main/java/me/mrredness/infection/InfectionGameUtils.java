package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import me.mrredness.infection.commands.MetaHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class InfectionGameUtils {

    static HashSet<Player> playersInGame = new HashSet<>();
    public static HashSet<Player> getPlayersInGame() {return playersInGame;}

    static HashMap<UUID, Location> playerPreviousLocation = new HashMap<>();

    static HashMap<UUID, ItemStack[]> playerPreviousInventory = new HashMap<>();

    static boolean lobbyStage = true;
    static BukkitTask barCountdown;

    static Integer minNumberOfPlayers = (Integer) DataHelper.get("Min Number of Players");
    public static Integer getMinNumberOfPlayers() {return minNumberOfPlayers;}
    public static void setMinNumberOfPlayers(Integer minNumberOfPlayers) {InfectionGameUtils.minNumberOfPlayers = minNumberOfPlayers;}

    static Integer maxNumberOfPlayers = (Integer) DataHelper.get("Min Number of Players");
    public static Integer getMaxNumberOfPlayers() {return maxNumberOfPlayers;}
    public static void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {InfectionGameUtils.maxNumberOfPlayers = maxNumberOfPlayers;}

    static HashSet<UUID> chosenInfected = new HashSet<>();
    public static HashSet<UUID> getChosenInfected() {return chosenInfected;}
    public static void addToInfected(Player p) {
        InfectionGameUtils.chosenInfected.add(p.getUniqueId());
        InfectionGameUtils.removeFromHider(p);
        InfectionGameUtils.removeFromRandom(p);
    }
    public static void removeFromInfected(Player p) {InfectionGameUtils.chosenInfected.remove(p.getUniqueId());}

    static HashSet<UUID> chosenHider = new HashSet<>();
    public static HashSet<UUID> getChosenHider() {return chosenHider;}
    public static void addToHider(Player p) {
        InfectionGameUtils.chosenHider.add(p.getUniqueId());
        InfectionGameUtils.removeFromInfected(p);
        InfectionGameUtils.removeFromRandom(p);
    }
    public static void removeFromHider(Player p) {InfectionGameUtils.chosenHider.remove(p.getUniqueId());}

    static HashSet<UUID> chosenRandom = new HashSet<>();
    public static HashSet<UUID> getChosenRandom() {return  chosenRandom;}
    public static void addToRandom(Player p) {
        InfectionGameUtils.chosenRandom.add(p.getUniqueId());
        InfectionGameUtils.removeFromInfected(p);
        InfectionGameUtils.removeFromHider(p);
    }
    public static void removeFromRandom(Player p) {InfectionGameUtils.chosenRandom.remove(p.getUniqueId());}


    public static void joinGame(Player p, boolean worldBorderEnabled, Infection plugin) {
        if (!playersInGame.contains(p)) {
            if (!lobbyStage) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, the game has already started.");
            } else {
                playerPreviousLocation.put(p.getUniqueId(), p.getLocation());
                playerPreviousInventory.put(p.getUniqueId(), p.getInventory().getStorageContents());
                p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
                p.getInventory().clear();
                playersInGame.add(p);
                p.sendMessage(ChatColor.GREEN + "Welcome to Infection!");
                int numberOfMorePlayersNeeded = minNumberOfPlayers - playersInGame.size();
                BarCountdown.setNumberOfMorePlayersNeeded(numberOfMorePlayersNeeded);
                BarCountdown.setPlayersInGame(playersInGame);
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
                    barCountdown = new BarCountdown(playersInGame, numberOfMorePlayersNeeded).runTaskAsynchronously(plugin);
                }
                if (DataHelper.checkBoolean("Allow Choice of Role")) {
                    ItemStack chooseRoleOpenInv = new ItemStack(Material.MAP, 1);
                    MetaHelper.setDisplayName(chooseRoleOpenInv, ChatColor.AQUA + "Choose your role in Infection!");
                    p.getInventory().addItem(chooseRoleOpenInv);
                }
            }
        }
        else {
            p.sendMessage(ChatColor.RED + "You are already in a game.");
        }
    }

    public static void leaveGame(Player p) {
        if (playersInGame.contains(p)) {
            playersInGame.remove(p);
            p.teleport(playerPreviousLocation.get(p.getUniqueId()));
            playerPreviousLocation.remove(p.getUniqueId());
            p.getInventory().clear();
            p.getInventory().setContents(playerPreviousInventory.get(p.getUniqueId()));
            playerPreviousInventory.remove(p.getUniqueId());
            BarCountdown.removePlayer(p);
            p.sendMessage(ChatColor.GREEN + "Bye! Play again soon!");
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

    public static void startGame() {
        lobbyStage = false;
        for (Player p : playersInGame) {
            p.sendMessage("Game has started");
            if (chosenInfected.contains(p.getUniqueId())) {
                becomeInfected(p);
            }
            else if (chosenHider.contains(p.getUniqueId())) {
                becomeHider(p);
            }
            else if (new Random().nextBoolean()) {
                becomeInfected(p);
            }
            else {
                becomeHider(p);
            }
        }
    }

    public static void becomeInfected(Player p) {
        p.teleport((Location) DataHelper.get("Infection Spawn Location"));
        p.setDisplayName(ChatColor.RED + p.getDisplayName());
    }

    public static void becomeHider(Player p) {
        p.teleport(TeleportUtils.findSafeLocation(DataHelper.getHashMap("Infection Border Range")));
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
