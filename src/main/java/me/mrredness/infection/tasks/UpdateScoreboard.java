package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGameUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.UUID;

public class UpdateScoreboard extends BukkitRunnable {

    Scoreboard scoreboard;

    public UpdateScoreboard(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    @Override
    public void run() {
        while (!InfectionGameUtils.isLobbyStage()) {
            int hiderLives = 0;
            for (UUID uuid : InfectionGameUtils.getHiders()) {
                hiderLives += InfectionGameUtils.getNumberOfLives().get(uuid);
            }

            int infectedLives = 0;
            for (UUID uuid : InfectionGameUtils.getInfected()) {
                infectedLives += InfectionGameUtils.getNumberOfLives().get(uuid);
            }

            Objective objective = scoreboard.registerNewObjective("Title", "dummy", ChatColor.stripColor("Infection!"));
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score hidersLeft = objective.getScore(ChatColor.GREEN + "Hiders Left: " + InfectionGameUtils.getHiders().size());
            hidersLeft.setScore(12);
            Score totalHiderLives = objective.getScore(ChatColor.GREEN + "Total Number of Lives for Hiders: " + hiderLives);
            totalHiderLives.setScore(11);
            Score blank = objective.getScore("");
            blank.setScore(10);
            Score infectedLeft = objective.getScore(ChatColor.RED + "Infected Left: " + InfectionGameUtils.getInfected().size());
            infectedLeft.setScore(9);
            Score totalInfectedLives = objective.getScore(ChatColor.RED + "Total Number of Lives for Infected: " + infectedLives);
            totalInfectedLives.setScore(8);


            for (Player p : InfectionGameUtils.getPlayersInGame()) {
                p.setScoreboard(scoreboard);
            }
        }
    }
}
