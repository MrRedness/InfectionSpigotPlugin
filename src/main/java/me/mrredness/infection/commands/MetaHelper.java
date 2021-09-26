package me.mrredness.infection.commands;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class MetaHelper {
    public static void setDisplayName(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(name);
        stack.setItemMeta(meta);
    }
    public static boolean checkDisplayName(ItemStack e, String title){
        return Objects.requireNonNull(e.getItemMeta()).getDisplayName().equalsIgnoreCase(title);
    }


}
