package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.AddPotionEffect;
import me.mrredness.infection.tasks.AsyncToSync.PlayerBecomeRoleTask;
import me.mrredness.infection.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ReleaseInfectedCountdown extends BukkitRunnable {
    static boolean running = false;

    public static boolean isRunning() {
        return running;
    }

    static PotionEffect blindness = new PotionEffect(PotionEffectType.BLINDNESS, 30 * 20, 255, false, false, false);

    @Override
    public void run() {
        running = true;
        for (Player p : InfectionGame.getPlayersInGame()) {
            if (InfectionGame.getInfected().contains(p.getUniqueId())) {
                new AddPotionEffect(blindness, p, false).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
                p.setInvulnerable(true);
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
            for (Player p : InfectionGame.getPlayersInGame()) {
                if (InfectionGame.getInfected().contains(p.getUniqueId())) {
                    p.setInvulnerable(false);
                    new AddPotionEffect(blindness, p, true).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
                    new PlayerBecomeRoleTask(true, p).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
                }
            }
        }
        running = false;
    }
}
