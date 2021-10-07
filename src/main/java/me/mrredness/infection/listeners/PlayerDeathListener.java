package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.helpers.DataHelper;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;


public class PlayerDeathListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player died = e.getEntity().getPlayer();
        if (InfectionGame.getPlayersInGame().contains(died)) {
            e.setKeepLevel(true);
            if (InfectionGame.isLobbyStage()) {
                e.setKeepInventory(true);
            }
            e.getDrops().clear();
            died.spigot().respawn();
        }
    }
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player respawned = e.getPlayer();
        if (InfectionGame.getPlayersInGame().contains(respawned)) {
            if (!InfectionGame.isLobbyStage()) {
                e.setRespawnLocation((Location) DataHelper.get("Infection Spawn Location"));
                Entity killer = respawned.getKiller();
                String killerName;
                if (killer == null) {
                    try {
                        killer = respawned.getLastDamageCause().getEntity();
                        killerName = killer.getName();
                    } catch (NullPointerException exception) {
                        killer = null;
                        killerName = "Unknown";
                    }
                } else {
                    killerName = killer.getName();
                }
                InfectionGame.death(respawned, killer, killerName);
            }
            else {
                e.setRespawnLocation((Location) DataHelper.get("Infection Lobby Spawn Location"));
            }
        }
    }
}
