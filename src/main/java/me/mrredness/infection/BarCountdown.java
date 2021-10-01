package me.mrredness.infection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class BarCountdown extends BukkitRunnable {

    static HashSet<Player> playersInGame;
    public static HashSet<Player> getPlayersInGame() {return playersInGame;}
    public static void setPlayersInGame(HashSet<Player> playersInGame) {BarCountdown.playersInGame = playersInGame;}


    static BossBar countdownBar = Bukkit.createBossBar("Waiting for Players", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    static boolean continueRunning = true;

    static boolean forceStart = false;

    public static void setForceStart(boolean forceStart) {
        BarCountdown.forceStart = forceStart;
    }

    static int numberOfSecondsUntilStart = 60;
    public static int getNumberOfSecondsUntilStart() {return numberOfSecondsUntilStart;}
    public static void setNumberOfSecondsUntilStart(int numberOfSecondsUntilStart) {BarCountdown.numberOfSecondsUntilStart = numberOfSecondsUntilStart;}

    static int numberOfMorePlayersNeeded;
    public static void setNumberOfMorePlayersNeeded(int numberOfMorePlayersNeeded) {BarCountdown.numberOfMorePlayersNeeded = numberOfMorePlayersNeeded;}


    public BarCountdown(HashSet<Player> playersInGame, int numberOfMorePlayersNeeded) {
        BarCountdown.playersInGame = playersInGame;
        BarCountdown.numberOfMorePlayersNeeded = numberOfMorePlayersNeeded;
    }

    @Override
    public void run() {
        while (playersInGame.size() > 0 && continueRunning) {
            countdownBar.removeAll();
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            if (numberOfMorePlayersNeeded < 1 || forceStart) {
                countdownBar.setTitle(BarColor.YELLOW + "Starting in " + BarColor.BLUE + numberOfSecondsUntilStart + " seconds!");
                countdownBar.setProgress( (double) numberOfSecondsUntilStart / 60);
                numberOfSecondsUntilStart--;
                if (numberOfSecondsUntilStart == 0) {
                    removeAll();
                    InfectionGameUtils.startGame();
                }
            }
            else if (numberOfMorePlayersNeeded == 1) {
                countdownBar.setTitle(ChatColor.RED + "Currently waiting for " + 1 + " more player.");
                numberOfSecondsUntilStart = 60;
            } else {
                countdownBar.setTitle(BarColor.YELLOW + "Currently waiting for " + BarColor.BLUE + numberOfMorePlayersNeeded + BarColor.YELLOW + " more players.");
                numberOfSecondsUntilStart = 60;
            }
            SleepUtils.one();
        }
        countdownBar.removeAll();
        super.cancel();
    }

    public static void removeAll() {
        countdownBar.removeAll();
        continueRunning = false;
    }

    public static void removePlayer(Player p){
        playersInGame.remove(p);
    }
}