package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.AddPotionEffect;
import me.mrredness.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ReleaseInfectedCountdown extends BukkitRunnable {
    public static boolean isRunning() {
        return running;
    }

    static boolean running = true;
    @Override
    public void run() {
        for (Player p : InfectionGame.getPlayersInGame()) {
            if (InfectionGame.getInfected().contains(p.getUniqueId())) {
                PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 30*20, 255, false, false, false);
                new AddPotionEffect(blindness, p).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            }
        }
        SleepUtils.wait(25000);
        if (!InfectionGame.isLobbyStage()) {
            int numberOfSecondsLeft = 5;
            while (numberOfSecondsLeft > 0) {
                for (Player p : InfectionGame.getPlayersInGame()) {
                    p.sendMessage(ChatColor.GOLD + "Infected released in " + numberOfSecondsLeft);
                }
                numberOfSecondsLeft--;
                SleepUtils.one();
            }
        }
        running = false;
    }
}
