package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.ReleaseInfectedCountdown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (ReleaseInfectedCountdown.isRunning() && !InfectionGame.isLobbyStage()) {
            if (InfectionGame.getInfected().contains(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}
