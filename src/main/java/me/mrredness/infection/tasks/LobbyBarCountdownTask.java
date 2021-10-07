package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.StartGameTask;
import me.mrredness.infection.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class LobbyBarCountdownTask extends BukkitRunnable {

    static HashSet<Player> playersInGame;


    static BossBar countdownBar = Bukkit.createBossBar("Waiting for Players", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    static boolean continueRunning = true;

    static boolean forceStart = false;
    static int numberOfSecondsUntilStart = 60;
    int numberOfMorePlayersNeeded;
    int minimumNumberOfPlayers;

    public LobbyBarCountdownTask(int minimumNumberOfPlayers) {
        this.minimumNumberOfPlayers = minimumNumberOfPlayers;
    }

    public static void setForceStart(boolean forceStart) {
        LobbyBarCountdownTask.forceStart = forceStart;
    }

    public static void setNumberOfSecondsUntilStart(int numberOfSecondsUntilStart) {
        LobbyBarCountdownTask.numberOfSecondsUntilStart = numberOfSecondsUntilStart;
    }

    public static void removeBar() {
        countdownBar.removeAll();
        continueRunning = false;
    }

    public static void removePlayer(Player p) {
        playersInGame.remove(p);
    }

    @Override
    public void run() {
        playersInGame = InfectionGame.getPlayersInGame();
        continueRunning = true;
        while (playersInGame.size() > 0 && continueRunning) {
            SleepUtils.one();
            playersInGame = InfectionGame.getPlayersInGame();
            numberOfMorePlayersNeeded = minimumNumberOfPlayers - playersInGame.size();
            countdownBar.removeAll();
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            if (numberOfMorePlayersNeeded < 1 || forceStart) {
                countdownBar.setTitle("Starting in " + numberOfSecondsUntilStart + " seconds!");
                countdownBar.setProgress((double) numberOfSecondsUntilStart / 60);
                numberOfSecondsUntilStart--;
                if (numberOfSecondsUntilStart == 0) {
                    removeBar();
                    forceStart = false;
                    numberOfSecondsUntilStart = 60;
                    new StartGameTask().runTask(Bukkit.getServer().getPluginManager().getPlugin("Infection"));
                }
            } else if (numberOfMorePlayersNeeded == 1) {
                countdownBar.setTitle(ChatColor.RED + "Currently waiting for " + 1 + " more player.");
                numberOfSecondsUntilStart = 60;
                countdownBar.setProgress(1);
            } else {
                countdownBar.setTitle(BarColor.YELLOW + "Currently waiting for " + BarColor.BLUE + numberOfMorePlayersNeeded + BarColor.YELLOW + " more players.");
                numberOfSecondsUntilStart = 60;
                countdownBar.setProgress(1);
            }
        }
        countdownBar.removeAll();
        super.cancel();
    }
}
