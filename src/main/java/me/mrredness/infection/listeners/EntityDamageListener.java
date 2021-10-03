package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGameUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerDamageReceive(EntityDamageByEntityEvent e) {
        if (!InfectionGameUtils.isLobbyStage()) {
            if (e.getEntity() instanceof Player) {
                Player damaged = (Player) e.getEntity();

                if (e.getDamager() instanceof Player) {
                    Player damager = (Player) e.getDamager();

                    if (InfectionGameUtils.getPlayersInGame().contains(damaged)) {
                        if ((damaged.getHealth() - e.getDamage()) <= 0) {

                            //Killed
                            e.setCancelled(true);
                            InfectionGameUtils.kill(damaged, damager);
                        }
                    }
                }
            }
        }
    }
}
