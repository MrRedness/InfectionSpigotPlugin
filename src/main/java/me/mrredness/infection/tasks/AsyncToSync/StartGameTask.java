package me.mrredness.infection.tasks.AsyncToSync;

import me.mrredness.infection.InfectionGame;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameTask extends BukkitRunnable {


    // this class is simply used as an intermediary to allow async task BarCountdown to run sync task Teleport in InfectionGameUtils.startGame()
    @Override
    public void run() {
        InfectionGame.startGame();
    }
}
