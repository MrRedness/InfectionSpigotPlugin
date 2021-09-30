package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class BarCountdown extends BukkitRunnable {

    HashSet<Player> playersInGame;
    boolean lobbyStage;
    static BossBar countdownBar = Bukkit.createBossBar("Waiting for Players", BarColor.YELLOW, BarStyle.SEGMENTED_10);

    public BarCountdown(HashSet<Player> playersInGame, boolean lobbyStage) {
        this.playersInGame = playersInGame;
        this.lobbyStage = lobbyStage;
    }

    @Override
    public void run() {
        while (playersInGame.size() > 0 && lobbyStage) {
            countdownBar.removeAll();
            for (Player p : playersInGame) {
                countdownBar.addPlayer(p);
            }
            int numberOfMorePlayersNeeded = (Integer) DataHelper.get("Min Number of Players") - playersInGame.size();
            if (numberOfMorePlayersNeeded == 1) {
                countdownBar.setTitle(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + 1 + ChatColor.GOLD + " more player.");
            } else {
                countdownBar.setTitle(ChatColor.GOLD + "Currently waiting for " + ChatColor.BLUE + numberOfMorePlayersNeeded + ChatColor.GOLD + " more players.");
            }
            SleepUtils.one();
        }
        countdownBar.removeAll();
        super.cancel();
    }

    public static void removeAll() {
        countdownBar.removeAll();
    }
}
