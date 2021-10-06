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

    static boolean continueRunning = true;


    static int secondsLeft = 10 * 60;


    @Override
    public void run() {
        while (playersInGame.size() > 0 && continueRunning) {
            playersInGame = InfectionGame.getPlayersInGame();
            countdownBar.removeAll();
            double minutesLeft = (double) secondsLeft / 60;
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            if (minutesLeft > 1) {
                countdownBar.setTitle(BarColor.YELLOW + "Time Left: " + BarColor.BLUE + (int) minutesLeft + " minutes!");
                countdownBar.setProgress(minutesLeft);
                secondsLeft--;
            } else {
                countdownBar.setTitle(BarColor.YELLOW + "Time Left: " + BarColor.BLUE + secondsLeft + " seconds.");
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
