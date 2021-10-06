package me.mrredness.infection.helpers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Objects;


public class ConfigHelper {

    private static final FileConfiguration config = Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Infection")).getConfig();

    public static ItemStack[] getInventory(boolean infectedInventory) {
        if (infectedInventory) {
            ArrayList<ItemStack> infectedInv = (ArrayList<ItemStack>) config.get("Infection Infected Inventory");
            return Objects.requireNonNull(infectedInv).toArray(new ItemStack[0]);
        } else {
            ArrayList<ItemStack> hiderInv = (ArrayList<ItemStack>) config.get("Infection Hider Inventory");
            return Objects.requireNonNull(hiderInv).toArray(new ItemStack[0]);
        }
    }
}
