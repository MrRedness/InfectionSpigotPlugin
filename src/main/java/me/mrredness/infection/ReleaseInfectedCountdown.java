package me.mrredness.infection;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ReleaseInfectedCountdown extends BukkitRunnable {
    static boolean keepRunning = true;
    @Override
    public void run() {
        while (keepRunning) {
            for (Player p : InfectionGameUtils.getPlayersInGame()) {
                if (InfectionGameUtils.getChosenInfected().contains(p)) {
                    PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS)
                    p.addPotionEffect()
                }
                else {

                }
            }
        }
    }
}
