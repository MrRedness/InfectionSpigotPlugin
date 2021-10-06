package me.mrredness.infection.tasks.AsyncToSync;


import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class AddPotionEffect extends BukkitRunnable {
    PotionEffect potionEffect;
    Player p;

    public AddPotionEffect(PotionEffect potionEffect, Player p) {
        this.potionEffect = potionEffect;
        this.p = p;
    }

    @Override
    public void run() {
        p.addPotionEffect(potionEffect);
    }
}
