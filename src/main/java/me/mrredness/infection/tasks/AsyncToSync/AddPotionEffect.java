package me.mrredness.infection.tasks.AsyncToSync;


import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class AddPotionEffect extends BukkitRunnable {
    PotionEffect potionEffect;
    Player p;
    boolean remove;

    public AddPotionEffect(PotionEffect potionEffect, Player p, boolean remove) {
        this.potionEffect = potionEffect;
        this.p = p;
        this.remove = remove;
    }

    @Override
    public void run() {
        if (remove) {
            p.removePotionEffect(potionEffect.getType());
        }
        else {
            p.addPotionEffect(potionEffect);
        }
    }
}
