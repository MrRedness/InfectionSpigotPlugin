package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReleaseInfectedCountdown extends BukkitRunnable {
    static boolean keepRunning = true;
    @Override
    public void run() {
        while (keepRunning) {
            for (Player p : InfectionGame.getPlayersInGame()) {
                if (InfectionGame.getInfected().contains(p)) {
             //       PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS)
             //       p.addPotionEffect()
                }
                else {

                }
            }
        }
    }
}
