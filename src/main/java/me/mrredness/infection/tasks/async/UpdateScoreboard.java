package me.mrredness.infection.tasks.async;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.utils.SleepUtils;
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
            timeLeft.setScore(11);
            Score blank1 = objective.getScore("");
            blank1.setScore(10);
            Score blank2 = objective.getScore("");
            blank2.setScore(8);
            Score hidersLeft = objective.getScore(ChatColor.GREEN + "Hiders Left:" + InfectionGame.getHiders().size());
            hidersLeft.setScore(7);
            Score totalHiderLives = objective.getScore(ChatColor.GREEN + "Hider Lives Left: " + hiderLives);
            totalHiderLives.setScore(6);
            Score blank3 = objective.getScore("");
            blank3.setScore(5);
            Score infectedLeft = objective.getScore(ChatColor.RED + "Infected Left:" + InfectionGame.getInfected().size());
            infectedLeft.setScore(4);
            Score totalInfectedLives = objective.getScore(ChatColor.RED + "Infected Lives Left: " + infectedLives);
            totalInfectedLives.setScore(3);
            Score blank4 = objective.getScore("");
            blank4.setScore(2);


            for (Player p : InfectionGame.getPlayersInGame()) {
                int numberOfLivesLeft = InfectionGame.getNumberOfLives().get(p.getUniqueId());
                boolean infected = InfectionGame.getInfected().contains(p.getUniqueId());
                Score playersLives;
                Score role;
                if (numberOfLivesLeft > 1) {
                    playersLives = objective.getScore(ChatColor.GOLD + "You have: " + numberOfLivesLeft + " lives left.");
                }
                else {
                    playersLives = objective.getScore(ChatColor.GOLD + "You have 1 life left.");
                }
                if (infected) {
                    role = objective.getScore(ChatColor.BLUE + "You are " + ChatColor.RED + "INFECTED!");
                }
                else {
                    role = objective.getScore(ChatColor.BLUE + "You are " + ChatColor.DARK_GREEN + "HIDER!");
                }
                role.setScore(9);
                playersLives.setScore(1);
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
