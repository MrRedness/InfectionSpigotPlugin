package me.mrredness.infection.tasks.AsyncToSync;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class SummonFirework extends BukkitRunnable{

    World world;
    Location location;
    Color[] colors;

    public SummonFirework(World world, Location location, Color[] colors) {
        this.world = world;
        this.location = location;
        this.colors = colors;
    }

    @Override
    public void run() {
       Firework firework = world.spawn(location, Firework.class);
       FireworkMeta fireworkMeta = firework.getFireworkMeta();
       fireworkMeta.addEffects(FireworkEffect.builder().withColor(colors).build());
       fireworkMeta.setPower(2);
       firework.setFireworkMeta(fireworkMeta);
    }
}
