package me.mrredness.infection.tasks.AsyncToSync;


import me.mrredness.infection.commands.InfectionCommand;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitForSecondAttemptTask extends BukkitRunnable {
    @Override
    public void run() {
        InfectionCommand.setSecondTimeRunningForceStart(false);
    }
    // this task has one and only one job
    // it allows InfectionCommand to wait a few seconds for a second attempt at running /infection forcestart in the case that there are too few players
}
