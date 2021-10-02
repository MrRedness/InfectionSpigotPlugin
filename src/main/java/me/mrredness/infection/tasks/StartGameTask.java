package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGameUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameTask extends BukkitRunnable {


    // this class is simply used as an intermediary to allow async task BarCountdown to run sync task Teleport in InfectionGameUtils.startGame()
    @Override
    public void run() {
        InfectionGameUtils.startGame();
    }
}
