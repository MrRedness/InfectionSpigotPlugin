package me.mrredness.infection.listeners;

import me.mrredness.infection.Infection;
import me.mrredness.infection.InfectionGame;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageListener implements Listener {
    Infection plugin;

    public EntityDamageListener(Infection plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void PlayerDamageReceive(EntityDamageByEntityEvent e) {
        if (!InfectionGame.isLobbyStage()) {
            if (e.getEntity() instanceof Player damaged) {
                if (InfectionGame.getPlayersInGame().contains(damaged)) {
                    Player damagerPlayer = damaged.getKiller();
                    String damagerName;
                    Entity damagerEntity = null;
                    if (damagerPlayer == null || damagerPlayer.equals(damaged)) {
                        try {
                            damagerName = e.getDamager().getName();
                            if (e.getDamager() instanceof Player p) {
                                damagerPlayer = p;
                            } else {
                                damagerPlayer = null;
                                damagerEntity = e.getDamager();
                            }
                        } catch (Exception exception) {
                            damagerName = damaged.getName();
                        }
                    } else {
                        damagerName = damagerPlayer.getName();
                    }
                    if (damagerPlayer != null) {
                        if ((InfectionGame.getHiders().contains(damagerPlayer.getUniqueId()) && InfectionGame.getHiders().contains(damaged.getUniqueId())) ||
                                (InfectionGame.getInfected().contains(damagerPlayer.getUniqueId()) && InfectionGame.getInfected().contains(damaged.getUniqueId()))) {
                            e.setCancelled(true);
                        } else if ((!e.isCancelled()) && ((damaged.getHealth() - e.getFinalDamage()) <= 0)) {
                            e.setCancelled(true);
                            InfectionGame.death(damaged, damagerPlayer, damagerName);
                        }
                    } else {
                        if ((damaged.getHealth() - e.getDamage()) <= 0) {
                            e.setCancelled(true);
                            InfectionGame.death(damaged, damagerEntity, damagerName);
                        }
                    }
                }
            }
        }
          else {
            if (e.getEntity() instanceof Player damaged) {
                if (InfectionGame.getPlayersInGame().contains(damaged)) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
