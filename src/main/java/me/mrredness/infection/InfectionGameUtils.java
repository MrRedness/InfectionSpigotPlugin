package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import me.mrredness.infection.commands.MetaHelper;
import me.mrredness.infection.tasks.BarCountdownTask;
import me.mrredness.infection.tasks.RespawnPlayerTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class InfectionGameUtils {

    private static final HashSet<Player> playersInGame = new HashSet<>();
    public static HashSet<Player> getPlayersInGame() {return playersInGame;}

    private static final HashMap<UUID, Location> playerPreviousLocation = new HashMap<>();

    private static final HashMap<UUID, ItemStack[]> playerPreviousInventory = new HashMap<>();
    private static final HashMap<UUID, ItemStack[]> playerPreviousArmor = new HashMap<>();

    private static boolean lobbyStage = true;
    public static boolean isLobbyStage() {return lobbyStage;}

    private static BukkitTask barCountdown;

    private static Integer minNumberOfPlayers = (Integer) DataHelper.get("Min Number of Players");
    public static Integer getMinNumberOfPlayers() {return minNumberOfPlayers;}
    public static void setMinNumberOfPlayers(Integer minNumberOfPlayers) {InfectionGameUtils.minNumberOfPlayers = minNumberOfPlayers;}

    private static Integer maxNumberOfPlayers = (Integer) DataHelper.get("Min Number of Players");
    public static Integer getMaxNumberOfPlayers() {return maxNumberOfPlayers;}
    public static void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {InfectionGameUtils.maxNumberOfPlayers = maxNumberOfPlayers;}

    private static final HashSet<UUID> chosenInfected = new HashSet<>();
    public static HashSet<UUID> getChosenInfected() {return chosenInfected;}
    public static void addToInfected(Player p) {
        InfectionGameUtils.chosenInfected.add(p.getUniqueId());
        InfectionGameUtils.removeFromHider(p);
        InfectionGameUtils.removeFromRandom(p);
    }
    public static void removeFromInfected(Player p) {InfectionGameUtils.chosenInfected.remove(p.getUniqueId());}

    private static final HashSet<UUID> chosenHider = new HashSet<>();
    public static HashSet<UUID> getChosenHider() {return chosenHider;}
    public static void addToHider(Player p) {
        InfectionGameUtils.chosenHider.add(p.getUniqueId());
        InfectionGameUtils.removeFromInfected(p);
        InfectionGameUtils.removeFromRandom(p);
    }
    public static void removeFromHider(Player p) {InfectionGameUtils.chosenHider.remove(p.getUniqueId());}

    private static final HashSet<UUID> chosenRandom = new HashSet<>();
    public static HashSet<UUID> getChosenRandom() {return  chosenRandom;}
    public static void addToRandom(Player p) {
        InfectionGameUtils.chosenRandom.add(p.getUniqueId());
        InfectionGameUtils.removeFromInfected(p);
        InfectionGameUtils.removeFromHider(p);
    }
    public static void removeFromRandom(Player p) {InfectionGameUtils.chosenRandom.remove(p.getUniqueId());}

    private static ItemStack[] infectedInv;
    private static ItemStack[] hiderInv;

    private static HashMap<UUID, Integer> numberOfLives = new HashMap<>();

    public static HashMap<UUID, Integer> getNumberOfLives() {return numberOfLives;}

    public static void setNumberOfLives(HashMap<UUID, Integer> numberOfLives) {InfectionGameUtils.numberOfLives = numberOfLives;}

    public static void joinGame(Player p, boolean worldBorderEnabled, Infection plugin) {
        if (!playersInGame.contains(p)) {
            if (!lobbyStage) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, the game has already started.");
            } else {
                playerPreviousLocation.put(p.getUniqueId(), p.getLocation());
                playerPreviousInventory.put(p.getUniqueId(), p.getInventory().getStorageContents());
                playerPreviousArmor.put(p.getUniqueId(), p.getInventory().getArmorContents());
                p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
                p.getInventory().clear();
                playersInGame.add(p);
                p.sendMessage(ChatColor.GREEN + "Welcome to Infection!");
                int numberOfMorePlayersNeeded = minNumberOfPlayers - playersInGame.size();
                BarCountdownTask.setNumberOfMorePlayersNeeded(numberOfMorePlayersNeeded);
                BarCountdownTask.setPlayersInGame(playersInGame);
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
                    barCountdown = new BarCountdownTask(playersInGame, numberOfMorePlayersNeeded).runTaskAsynchronously(plugin);
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
            p.getInventory().setArmorContents(playerPreviousArmor.get(p.getUniqueId()));
            p.setDisplayName(p.getName());
            playerPreviousInventory.remove(p.getUniqueId());
            BarCountdownTask.removePlayer(p);
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
            else if ((getChosenInfected().size() / (double) playersInGame.size()) >= 0.5) {
                becomeHider(p);
            }
            else if ((getChosenHider().size() / (double) playersInGame.size()) >= 0.5) {
                addToInfected(p);
                becomeInfected(p);
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
        addToInfected(p);
        p.teleport((Location) DataHelper.get("Infection Spawn Location"));
        p.setPlayerListName(ChatColor.RED + "Infected: " + ChatColor.YELLOW + p.getName());
        p.setDisplayName(ChatColor.RED + "Infected: " + ChatColor.YELLOW + p.getName() + ChatColor.WHITE);
        p.getInventory().clear();
        p.getInventory().setContents(infectedInv);
        numberOfLives.put(p.getUniqueId(), 2);
    }

    public static void becomeHider(Player p) {
        addToHider(p);
        p.teleport(TeleportUtils.findSafeLocation(DataHelper.getHashMap("Infection Border Range")));
        p.setPlayerListName(ChatColor.AQUA + "Hider: " + ChatColor.YELLOW + p.getName());
        p.setDisplayName(ChatColor.AQUA + "Hider: " + ChatColor.YELLOW + p.getName() + ChatColor.WHITE);
        p.getInventory().clear();
        p.getInventory().setContents(hiderInv);
        numberOfLives.put(p.getUniqueId(), 1);
    }

    public static void kill(Player damaged, Player damager) {
        int numberOfLivesLeft = numberOfLives.get(damaged.getUniqueId());
        if (chosenHider.contains(damaged.getUniqueId())) {
            if (numberOfLivesLeft == 1) {
                BukkitTask respawn = new RespawnPlayerTask(damaged, true, 2).runTaskAsynchronously(Bukkit.getServer().getPluginManager().getPlugin("Infection"));
                numberOfLives.replace(damaged.getUniqueId(), 2);
            }
            else {
                BukkitTask respawn = new RespawnPlayerTask(damaged, false, numberOfLivesLeft).runTaskAsynchronously(Bukkit.getServer().getPluginManager().getPlugin("Infection"));
                numberOfLives.replace(damaged.getUniqueId(), numberOfLivesLeft - 1);
            }
        }
        else {
            if (numberOfLivesLeft == 1) {
                damaged.setGameMode(GameMode.SPECTATOR);
                damaged.sendMessage(ChatColor.RED + "You ran out of lives and are now spectating.");
            }
            else {
                BukkitTask respawn = new RespawnPlayerTask(damaged, true, numberOfLivesLeft).runTaskAsynchronously(Bukkit.getServer().getPluginManager().getPlugin("Infection"));
                numberOfLives.replace(damaged.getUniqueId(), numberOfLivesLeft - 1);
            }
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
