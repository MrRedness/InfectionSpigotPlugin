package me.mrredness.infection.tasks;

import me.mrredness.infection.tasks.AsyncToSync.PlayerBecomeRoleTask;
import me.mrredness.infection.tasks.AsyncToSync.PlayerChangeGamemodeTask;
import me.mrredness.utils.SleepUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class RespawnPlayerTask extends BukkitRunnable {

    Player p;
    boolean infected;
    boolean infectedForFirstTime;
    int numberOfSecondsLeft = 5;
    int numberOfLivesLeft;

    public RespawnPlayerTask(Player p, boolean infected, boolean infectedForFirstTime, int numberOfLivesLeft) {
        this.p = p;
        this.infected = infected;
        this.infectedForFirstTime = infectedForFirstTime;
        this.numberOfLivesLeft = numberOfLivesLeft;
    }

    @Override
    public void run() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Infection");
        new PlayerChangeGamemodeTask(GameMode.SPECTATOR, p).runTask(plugin);
        if (infectedForFirstTime) {
            p.sendMessage(ChatColor.RED + "You are now infected.");
        }
        p.sendMessage(ChatColor.LIGHT_PURPLE + "You have " + numberOfLivesLeft + " lives left.");
        while (numberOfSecondsLeft > 0) {
            p.sendMessage(ChatColor.GREEN + String.valueOf(numberOfSecondsLeft) + " seconds left to respawn.");
            SleepUtils.one();
            numberOfSecondsLeft--;
        }
        if (infected) {
            //become infected
            new PlayerBecomeRoleTask(true, p).runTask(plugin);
        }
        else {
            //become hider
            new PlayerBecomeRoleTask(false, p).runTask(plugin);
        }
        new PlayerChangeGamemodeTask(GameMode.ADVENTURE, p).runTask(plugin);
    }
}
