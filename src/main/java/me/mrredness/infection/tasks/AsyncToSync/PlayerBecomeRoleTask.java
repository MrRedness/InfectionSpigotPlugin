package me.mrredness.infection.tasks.AsyncToSync;

import me.mrredness.infection.InfectionGame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerBecomeRoleTask extends BukkitRunnable {
// allows async task to call becomeHider or becomeInfected
    boolean Infected;
    Player p;

    public PlayerBecomeRoleTask(boolean infected, Player p) {
        Infected = infected;
        this.p = p;
    }

    @Override
    public void run() {
        if (Infected) {
            InfectionGame.becomeInfected(p);
        }
        else {
            InfectionGame.becomeHider(p);
        }
    }
}
