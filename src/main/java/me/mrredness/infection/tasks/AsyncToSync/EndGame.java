package me.mrredness.infection.tasks.AsyncToSync;

import me.mrredness.infection.InfectionGame;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGame extends BukkitRunnable {
    //allows async task to endgame
    String reasonToEnd;
    boolean fireworks;

    public EndGame(String reasonToEnd, boolean fireworks) {
        this.reasonToEnd = reasonToEnd;
        this.fireworks = fireworks;
    }

    @Override
    public void run() {
        InfectionGame.endGame(reasonToEnd, fireworks);
    }
}
