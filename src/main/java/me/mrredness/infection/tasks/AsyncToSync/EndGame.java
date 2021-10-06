package me.mrredness.infection.tasks.AsyncToSync;

import me.mrredness.infection.InfectionGame;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGame extends BukkitRunnable {
    //allows async task to endgame
    String reasonToEnd;

    public EndGame(String reasonToEnd) {
        this.reasonToEnd = reasonToEnd;
    }

    @Override
    public void run() {
        InfectionGame.endGame(reasonToEnd);
    }
}
