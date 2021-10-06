package me.mrredness.infection.tasks.AsyncToSync;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerChangeGamemodeTask extends BukkitRunnable {
    public PlayerChangeGamemodeTask(GameMode newGamemode, Player p) {
        this.newGamemode = newGamemode;
        this.p = p;
    }

    // this class is simply used as an intermediary to allow async task to run sync-only method changeGamemode
    GameMode newGamemode;
    Player p;
    @Override
    public void run() {
        p.setGameMode(newGamemode);
    }
}
