package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.StartGameTask;
import me.mrredness.infection.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class GameBarCountdownTask extends BukkitRunnable {

    static HashSet<Player> playersInGame;


    static BossBar countdownBar = Bukkit.createBossBar("Infection!", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    public static void removeBar() {
        continueRunning = false;
        countdownBar.removeAll();
    }

    static boolean continueRunning = true;


    static int secondsLeft = 10 * 60;


    @Override
    public void run() {
        playersInGame = InfectionGame.getPlayersInGame();
        continueRunning = true;
        while (playersInGame.size() > 0 && continueRunning && !InfectionGame.isLobbyStage()) {
            playersInGame = InfectionGame.getPlayersInGame();
            double minutesLeft = (double) secondsLeft / 60;
            countdownBar.removeAll();
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            if (minutesLeft > 1) {
                countdownBar.setTitle("Time Left: " + (int) minutesLeft + " minutes!");
                countdownBar.setProgress(minutesLeft / 10);
                secondsLeft--;
            } else {
                countdownBar.setTitle("Time Left: " + secondsLeft + " seconds.");
                secondsLeft--;
                if (secondsLeft == 0) {
                    continueRunning = false;
                    new StartGameTask().runTask(Bukkit.getServer().getPluginManager().getPlugin("Infection"));
                }
            }
            SleepUtils.one();
        }
        countdownBar.removeAll();
        super.cancel();
    }
}
