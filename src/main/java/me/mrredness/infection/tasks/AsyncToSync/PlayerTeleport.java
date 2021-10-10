package me.mrredness.infection.tasks.AsyncToSync;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTeleport extends BukkitRunnable {

    Player p;
    Location location;

    public PlayerTeleport(Player p, Location location) {
        this.p = p;
        this.location = location;
    }

    @Override
    public void run() {
        p.teleport(location);
    }
}
