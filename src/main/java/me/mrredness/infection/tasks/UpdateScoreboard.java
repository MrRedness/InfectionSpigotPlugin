package me.mrredness.infection.tasks;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.utils.SleepUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;
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
            if (scoreboard.getObjective("Infection") != null) {
                Objects.requireNonNull(scoreboard.getObjective("Infection")).unregister();
            }
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
            timeLeft.setScore(8);
            Score hidersLeft = objective.getScore(ChatColor.GREEN + "Hiders Left:" + InfectionGame.getHiders().size());
            hidersLeft.setScore(7);
            Score totalHiderLives = objective.getScore(ChatColor.GREEN + "Hider Lives Left: " + hiderLives);
            totalHiderLives.setScore(6);
            Score blank = objective.getScore("");
            blank.setScore(5);
            Score infectedLeft = objective.getScore(ChatColor.RED + "Infected Left:" + InfectionGame.getInfected().size());
            infectedLeft.setScore(4);
            Score totalInfectedLives = objective.getScore(ChatColor.RED + "Infected Lives Left: " + infectedLives);
            totalInfectedLives.setScore(3);
            Score blank2 = objective.getScore("");
            blank2.setScore(2);


            /*for (Player p : InfectionGame.getPlayersInGame()) {
                int numberOfLivesLeft = InfectionGame.getNumberOfLives().get(p.getUniqueId());
                Score playersLives;
                if (numberOfLivesLeft > 1) {
                    playersLives = objective.getScore(ChatColor.GOLD + "You have " + numberOfLivesLeft + " lives left.");
                } else {
                    playersLives = objective.getScore(ChatColor.GOLD + "You have 1 life left.");
                }
                playersLives.setScore(1);
                p.setScoreboard(scoreboard);
            } */
            for (Player p : InfectionGame.getPlayersInGame()) {
                p.setScoreboard(scoreboard);
            }
            secondsLeft--;
            if (secondsLeft < 0) {
                secondsLeft = 59;
                minutesLeft--;
            }
            SleepUtils.one();
           // Score blank5 = objective.getScore("");
           // blank5.setScore(1);
            if (minutesLeft < 0) {
                InfectionGame.endGame(ChatColor.GREEN + "Time ran out. Hiders win!", true);
            }
            objective.unregister();
        }
    }
}
