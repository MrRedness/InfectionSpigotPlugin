package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.EndGame;
import me.mrredness.infection.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class EndGameWhenOneTeamIsEliminated extends BukkitRunnable {
    @Override
    public void run() {
    /*    while (!InfectionGame.isLobbyStage()) {
            if (InfectionGame.getHiders().isEmpty()) {
                new EndGame(ChatColor.DARK_BLUE + "All hiders eliminated. Infected win!!", true).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            } else if (InfectionGame.getInfected().isEmpty()) {
                new EndGame(ChatColor.GOLD + "All infected eliminated. Hiders win!!", true).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            }
            SleepUtils.one(); //just to save performance
        } */
    }
}
