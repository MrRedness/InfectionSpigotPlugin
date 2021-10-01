package me.mrredness.infection.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

public class PlayerUseListener implements Listener {
    @EventHandler
    public void onUse(PlayerItemConsumeEvent e) {
        if (e.getItem().getType().equals(Material.MUSHROOM_STEM)) {
            e.setCancelled(true);
        }
    }
}
