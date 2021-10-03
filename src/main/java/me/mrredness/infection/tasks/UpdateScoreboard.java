package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.utils.SleepUtils;
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
        int minutesLeft = 10;
        int secondsLeft = 0;
        while (!InfectionGame.isLobbyStage()) {
            Objective objective = scoreboard.registerNewObjective("Infection", "dummy", ChatColor.GOLD + "Infection!");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            int hiderLives = 0;
            for (UUID uuid : InfectionGame.getHiders()) {
                hiderLives += InfectionGame.getNumberOfLives().get(uuid);
            }

            int infectedLives = 0;
            for (UUID uuid : InfectionGame.getInfected()) {
                infectedLives += InfectionGame.getNumberOfLives().get(uuid);
            }

            Score timeLeft = objective.getScore(ChatColor.LIGHT_PURPLE + String.valueOf(minutesLeft) + " minutes & " + secondsLeft + " seconds left.");
            timeLeft.setScore(6);
            Score hidersLeft = objective.getScore(ChatColor.GREEN + "Hiders Left:" + InfectionGame.getHiders().size());
            hidersLeft.setScore(5);
            Score totalHiderLives = objective.getScore(ChatColor.GREEN + "Hider Lives Left: " + hiderLives);
            totalHiderLives.setScore(4);
            Score blank = objective.getScore("");
            blank.setScore(3);
            Score infectedLeft = objective.getScore(ChatColor.RED + "Infected Left:" + InfectionGame.getInfected().size());
            infectedLeft.setScore(2);
            Score totalInfectedLives = objective.getScore(ChatColor.RED + "Infected Lives Left: " + infectedLives);
            totalInfectedLives.setScore(1);


            for (Player p : InfectionGame.getPlayersInGame()) {
                p.setScoreboard(scoreboard);
            }
            secondsLeft --;
            if (secondsLeft < 0) {
                secondsLeft = 59;
                minutesLeft --;
            }
            SleepUtils.one();
            objective.unregister();
            if (minutesLeft < 0) {
                InfectionGame.endGame(ChatColor.GREEN + "Time ran out. Hiders win!");
            }
        }
    }
}
