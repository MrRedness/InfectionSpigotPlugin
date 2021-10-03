package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerDamageReceive(EntityDamageByEntityEvent e) {
        if (!InfectionGame.isLobbyStage()) {
            if (e.getEntity() instanceof Player damaged) {
                if (InfectionGame.getPlayersInGame().contains(damaged)) {
                    if ((damaged.getHealth() - e.getDamage()) <= 0) {
                        e.setCancelled(true);
                        InfectionGame.death(damaged);
                    }
                }
            }
        }
    }
}
