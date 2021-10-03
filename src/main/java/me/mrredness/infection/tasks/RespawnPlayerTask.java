package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGameUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RespawnPlayerTask extends BukkitRunnable {

    Player p;
    boolean infected;
    int numberOfSecondsLeft = 5;
    int numberOfLivesLeft;

    public RespawnPlayerTask(Player p, boolean infected, int numberOfLivesLeft) {
        this.p = p;
        this.infected = infected;
        this.numberOfLivesLeft = numberOfLivesLeft;
    }

    @Override
    public void run() {
        p.setGameMode(GameMode.SPECTATOR);
        if (infected) {
            p.sendMessage(ChatColor.RED + "You are now infected.");
            p.sendMessage(ChatColor.GREEN + "You have " + numberOfLivesLeft + " lives left.");
        }
        else {
            InfectionGameUtils.becomeHider(p);
        }
        while (numberOfSecondsLeft > 0) {
            p.sendMessage(ChatColor.GREEN + String.valueOf(numberOfSecondsLeft) + " seconds left to respawn.");
        }
        if (infected) {
            InfectionGameUtils.becomeInfected(p);
        }
        else {
            InfectionGameUtils.becomeHider(p);
        }
    }
}
