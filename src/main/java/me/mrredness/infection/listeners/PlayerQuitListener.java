package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGameUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerQuitListener implements Listener {
    @EventHandler
    public void onMenuClick(PlayerQuitEvent e) {
        InfectionGameUtils.leaveGame(e.getPlayer());
        try {
            e.wait(1);
        } catch (InterruptedException ignored) {}
    }
}
