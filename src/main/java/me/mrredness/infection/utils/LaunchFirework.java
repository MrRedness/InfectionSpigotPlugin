package me.mrredness.infection.utils;


import me.mrredness.infection.tasks.AsyncToSync.SummonFirework;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class LaunchFirework extends BukkitRunnable {

    static Firework firework;
    World world;
    HashMap<String, Integer> locationRange;
    Location location;
    int numberOfFireworks;
    int delayBetweenLaunchesInMilliseconds;
    Color[] colors;
    boolean range;

    public LaunchFirework(World world, HashMap<String, Integer> locationRange, int numberOfFireworks, int delayBetweenLaunchesInMilliseconds, Color[] colors) {
        this.world = world;
        this.locationRange = locationRange;
        this.numberOfFireworks = numberOfFireworks;
        this.delayBetweenLaunchesInMilliseconds = delayBetweenLaunchesInMilliseconds;
        this.colors = colors;
        range = true;
    }

    public LaunchFirework(Location location, int numberOfFireworks, int delayBetweenLaunchesInMilliseconds, Color[] colors) {
        this.location = location;
        this.numberOfFireworks = numberOfFireworks;
        this.delayBetweenLaunchesInMilliseconds = delayBetweenLaunchesInMilliseconds;
        this.colors = colors;
        range = false;
    }

    public static void setFirework(Firework firework) {
        LaunchFirework.firework = firework;
    }

    public static void launch(World world, HashMap<String, Integer> locationRange, int numberOfFireworks, int delayBetweenLaunchesInMilliseconds, Color[] colors) {
        while (numberOfFireworks > 0) {
            new SummonFirework(world, TeleportUtils.findSafeLocation(locationRange), colors).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            numberOfFireworks--;
            SleepUtils.wait(delayBetweenLaunchesInMilliseconds);
        }
    }

    public static void launch(World world, Location location, int numberOfFireworks, int delayBetweenLaunchesInMilliseconds, Color[] colors) {
        while (numberOfFireworks > 0) {
            new SummonFirework(world, location, colors).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            numberOfFireworks--;
            SleepUtils.wait(delayBetweenLaunchesInMilliseconds);
        }
    }

    @Override
    public void run() {
        if (range) {
            launch(world, locationRange, numberOfFireworks, delayBetweenLaunchesInMilliseconds, colors);
        } else {
            launch(location.getWorld(), location, numberOfFireworks, delayBetweenLaunchesInMilliseconds, colors);
        }
    }
}
