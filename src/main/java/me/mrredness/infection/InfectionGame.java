package me.mrredness.infection;

import me.mrredness.infection.utils.LaunchFirework;
import me.mrredness.infection.helpers.ConfigHelper;
import me.mrredness.infection.helpers.DataHelper;
import me.mrredness.infection.helpers.MetaHelper;
import me.mrredness.infection.tasks.*;
import me.mrredness.infection.utils.BorderUtils;
import me.mrredness.infection.utils.TeleportUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class InfectionGame {

    private static final HashSet<Player> playersInGame = new HashSet<>();

    public static HashSet<Player> getPlayersInGame() {
        return playersInGame;
    }

    private static final HashMap<UUID, Location> playerPreviousLocation = new HashMap<>();

    private static final HashMap<UUID, ItemStack[]> playerPreviousInventory = new HashMap<>();

    private static final HashMap<UUID, GameMode> playerPreviousGamemode = new HashMap<>();

    private static boolean lobbyStage = true;

    public static boolean isLobbyStage() {
        return lobbyStage;
    }

    private static BukkitTask barCountdown;

    private static Integer minNumberOfPlayers = (Integer) DataHelper.get("Min Number of Players");

    public static Integer getMinNumberOfPlayers() {
        return minNumberOfPlayers;
    }

    public static void setMinNumberOfPlayers(Integer minNumberOfPlayers) {
        InfectionGame.minNumberOfPlayers = minNumberOfPlayers;
    }

    private static Integer maxNumberOfPlayers = (Integer) DataHelper.get("Max Number of Players");

    public static void setMaxNumberOfPlayers(Integer maxNumberOfPlayers) {
        InfectionGame.maxNumberOfPlayers = maxNumberOfPlayers;
    }

    private static final HashSet<UUID> infected = new HashSet<>();

    public static HashSet<UUID> getInfected() {
        return infected;
    }

    public static void addToInfected(Player p) {
        InfectionGame.infected.add(p.getUniqueId());
        InfectionGame.removeFromHider(p);
        InfectionGame.removeFromRandom(p);
    }

    public static void removeFromInfected(Player p) {
        InfectionGame.infected.remove(p.getUniqueId());
    }

    private static final HashSet<UUID> hiders = new HashSet<>();

    public static HashSet<UUID> getHiders() {
        return hiders;
    }

    public static void addToHider(Player p) {
        InfectionGame.hiders.add(p.getUniqueId());
        InfectionGame.removeFromInfected(p);
        InfectionGame.removeFromRandom(p);
    }

    public static void removeFromHider(Player p) {
        InfectionGame.hiders.remove(p.getUniqueId());
    }

    private static final HashSet<UUID> chosenRandom = new HashSet<>();

    public static HashSet<UUID> getChosenRandom() {
        return chosenRandom;
    }

    public static void addToRandom(Player p) {
        InfectionGame.chosenRandom.add(p.getUniqueId());
        InfectionGame.removeFromInfected(p);
        InfectionGame.removeFromHider(p);
    }

    public static void removeFromRandom(Player p) {
        InfectionGame.chosenRandom.remove(p.getUniqueId());
    }

    private static final ItemStack[] infectedInv = ConfigHelper.getInventory(true);
    private static final ItemStack[] hiderInv = ConfigHelper.getInventory(false);

    private static final HashMap<UUID, Integer> numberOfLives = new HashMap<>();

    public static HashMap<UUID, Integer> getNumberOfLives() {
        return numberOfLives;
    }

    private static boolean worldBorderEnabled;

    private static Infection plugin;

    private static final Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();

    public static void heal(Player p){
        p.setHealth(20);
        p.setFoodLevel(20);
        p.setSaturation(20);
    }

    public static void joinGame(Player p, boolean worldBorderEnabled, Infection plugin) {
        InfectionGame.worldBorderEnabled = worldBorderEnabled;
        InfectionGame.plugin = plugin;
        if (!playersInGame.contains(p)) {
            if (!lobbyStage) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, the game has already started.");
            } else if (playersInGame.size() == maxNumberOfPlayers) {
                p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, the game is full.");
            } else {
                playerPreviousLocation.put(p.getUniqueId(), p.getLocation());
                playerPreviousInventory.put(p.getUniqueId(), p.getInventory().getContents());
                playerPreviousGamemode.put(p.getUniqueId(), p.getGameMode());
                p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
                p.getInventory().clear();
                p.setGameMode(GameMode.ADVENTURE);
                heal(p);
                playersInGame.add(p);
                p.sendMessage(ChatColor.GREEN + "Welcome to Infection!");
                int numberOfMorePlayersNeeded = minNumberOfPlayers - playersInGame.size();
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
                    barCountdown = new LobbyBarCountdownTask(minNumberOfPlayers).runTaskAsynchronously(plugin);
                }
                if (DataHelper.checkBoolean("Allow Choice of Role")) {
                    ItemStack chooseRoleOpenInv = new ItemStack(Material.MAP, 1);
                    MetaHelper.setDisplayName(chooseRoleOpenInv, ChatColor.AQUA + "Choose your role in Infection!");
                    p.getInventory().addItem(chooseRoleOpenInv);
                }
            }
        } else {
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
            p.setDisplayName(p.getName());
            p.setGameMode(playerPreviousGamemode.get(p.getUniqueId()));
            heal(p);
            playerPreviousInventory.remove(p.getUniqueId());
            LobbyBarCountdownTask.removePlayer(p);
            p.sendMessage(ChatColor.GREEN + "Bye! Play again soon!");
            if (playersInGame.size() == 0) {
                BorderUtils.removeBorder("Infection Lobby Border Range", "Infection Lobby World");
                barCountdown.cancel();
            }
        } else {
            p.sendMessage(ChatColor.RED + "You are not currently in a game");
        }
    }

    public static String randomJoinMessage(Player p) {
        Random random = new Random();
        if (random.nextBoolean()) {
            if (random.nextBoolean()) {
                return "How nice of you to join us, " + p.getDisplayName() + ".";
            } else {
                return "We have been graced by the presence of " + p.getDisplayName() + ".";
            }
        } else {
            if (random.nextBoolean()) {
                return p.getDisplayName() + " slid into your DMs!";
            } else {
                return "Yay, " + p.getDisplayName() + " made it!";
            }
        }
    }

    public static void startGame() {
        lobbyStage = false;
        if (worldBorderEnabled) {
            BorderUtils.removeBorder("Infection Lobby Setup Complete", "Infection Lobby World");
        }
        for (Player p : playersInGame) {
            if (infected.contains(p.getUniqueId())) {
                becomeInfectedForFirstTime(p);
            } else if (hiders.contains(p.getUniqueId())) {
                becomeHiderForFirstTime(p);
            } else if ((getInfected().size() / (double) playersInGame.size()) >= 0.5) {
                becomeHider(p);
            } else if ((getHiders().size() / (double) playersInGame.size()) >= 0.5) {
                becomeInfectedForFirstTime(p);
            } else if (new Random().nextBoolean()) {
                becomeInfectedForFirstTime(p);

            } else {
                becomeHiderForFirstTime(p);

            }
        }
        new UpdateScoreboard(scoreboard).runTaskAsynchronously(plugin);
        new EndGameWhenOneTeamIsEliminated().runTaskAsynchronously(plugin);
        new ReleaseInfectedCountdown().runTaskAsynchronously(plugin);
    }

    public static void becomeInfected(Player p) {
        addToInfected(p);
        p.teleport((Location) DataHelper.get("Infection Spawn Location"));
        p.setPlayerListName(ChatColor.RED + "Infected: " + ChatColor.YELLOW + p.getName());
        p.setDisplayName(ChatColor.RED + "Infected: " + ChatColor.YELLOW + p.getName() + ChatColor.WHITE);
        p.getInventory().clear();
        p.getInventory().setContents(infectedInv);
        heal(p);
    }
    public static void becomeInfectedForFirstTime(Player p) {
        becomeInfected(p);
        numberOfLives.put(p.getUniqueId(), 2);
        p.sendMessage(ChatColor.RED + "You are INFECTED. You will be released in 30 seconds to hunt down and kill all the hiders.");
        p.sendMessage(ChatColor.DARK_GREEN + "However, if they kill you, they will gain an extra life (up to 3). You only have 2 life, so be careful.");
        p.sendMessage(ChatColor.LIGHT_PURPLE + "Any hider who runs out of lives will become INFECTED.");
        p.sendMessage(ChatColor.GOLD + "Good luck!");
    }

    public static void becomeHider(Player p) {
        addToHider(p);
        p.teleport(TeleportUtils.findSafeLocation(DataHelper.getHashMap("Infection Border Range")));
        p.setPlayerListName(ChatColor.AQUA + "Hider: " + ChatColor.YELLOW + p.getName());
        p.setDisplayName(ChatColor.AQUA + "Hider: " + ChatColor.YELLOW + p.getName() + ChatColor.WHITE);
        p.getInventory().clear();
        p.getInventory().setContents(hiderInv);
        heal(p);
    }
    public static void becomeHiderForFirstTime(Player p){
        becomeHider(p);
        numberOfLives.put(p.getUniqueId(), 1);
        p.sendMessage(ChatColor.GREEN + "You are a hider. The hunters will be released in 30 seconds to hunt down and kill all the hiders.");
        p.sendMessage(ChatColor.DARK_GREEN + "However, if you kill them, you will gain an extra life (up to 3). They only have 2 lives, so try to team up and take them out.");
        p.sendMessage(ChatColor.LIGHT_PURPLE + "Any hider who runs out of lives will become INFECTED.");
        p.sendMessage(ChatColor.GOLD + "Good luck!");
    }

    public static void death(Player killed) {
        int numberOfLivesLeft = InfectionGame.numberOfLives.get(killed.getUniqueId()) - 1;
        Player killer = killed.getKiller();
        for (Player p : playersInGame) {
            if (p.equals(killed)){
                p.sendMessage(ChatColor.RED + "You were killed by " + ChatColor.GOLD + killer.getName());
            }
            else if (p.equals(killer)){
                p.sendMessage(ChatColor.LIGHT_PURPLE + "You killed " + ChatColor.GOLD + killed.getName());            }
            else {
                p.sendMessage(ChatColor.RED + killed.getDisplayName() + ChatColor.DARK_PURPLE + " was killed by " + ChatColor.GOLD + killer.getDisplayName());
            }
        }
        if (hiders.contains(killed.getUniqueId())) {
            if (numberOfLivesLeft == 0) {
                numberOfLivesLeft = 2;
                new RespawnPlayerTask(killed, true, true, numberOfLivesLeft).runTaskAsynchronously(plugin);
                numberOfLives.replace(killed.getUniqueId(), numberOfLivesLeft);
            } else {
                new RespawnPlayerTask(killed, false, false, numberOfLivesLeft).runTaskAsynchronously(plugin);
                numberOfLives.replace(killed.getUniqueId(), numberOfLivesLeft);
            }
        } else {
            if (numberOfLivesLeft == 0) {
                killed.setGameMode(GameMode.SPECTATOR);
                killed.sendMessage(ChatColor.RED + "You ran out of lives and are now spectating.");
                infected.remove(killed.getUniqueId());
            } else {
                new RespawnPlayerTask(killed, true, false, numberOfLivesLeft).runTaskAsynchronously(plugin);
                InfectionGame.numberOfLives.replace(killed.getUniqueId(), numberOfLivesLeft);
            }
        }
        int damagerNumberOfLives;
        if (killer != null) {
            damagerNumberOfLives = numberOfLives.get(killer.getUniqueId());
            if (hiders.contains(killer.getUniqueId())) {
                if (damagerNumberOfLives < 3) {
                    numberOfLives.replace(killer.getUniqueId(), damagerNumberOfLives + 1);
                }
            }
        }
//        else if (damagerNumberOfLives < 2) {
//            numberOfLives.replace(damager.getUniqueId(), damagerNumberOfLives + 1);
//        }
    }


    public static boolean endGame(String reason) {
        if (!lobbyStage) {
            lobbyStage = true;
            infected.clear();
            hiders.clear();
            chosenRandom.clear();
            numberOfLives.clear();

            for (Player p : playersInGame) {
                p.sendMessage(reason);
                p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
                p.getInventory().clear();
                p.teleport((Location) DataHelper.get("Infection Lobby Spawn Location"));
                if (worldBorderEnabled) {
                    BorderUtils.setBorder("Infection Lobby Border Range", "Infection Lobby World");
                }
                int numberOfMorePlayersNeeded = minNumberOfPlayers - playersInGame.size();
                barCountdown = new LobbyBarCountdownTask(minNumberOfPlayers).runTaskAsynchronously(plugin);
                if (DataHelper.checkBoolean("Allow Choice of Role")) {
                    ItemStack chooseRoleOpenInv = new ItemStack(Material.MAP, 1);
                    MetaHelper.setDisplayName(chooseRoleOpenInv, ChatColor.AQUA + "Choose your role in Infection!");
                    p.getInventory().addItem(chooseRoleOpenInv);
                }
            }
            new LaunchFirework( Bukkit.getWorld((String) DataHelper.get("Infection Spawn World")), DataHelper.getHashMap("Infection Border Range"),
                    7, 1000, new Color[]{Color.RED, Color.BLUE}).runTaskAsynchronously(plugin);
            return true;
        }
        return false;
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
