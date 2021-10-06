package me.mrredness.infection.helpers;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.Objects;

public class MetaHelper {
    public static void setDisplayName(ItemStack stack, String name) {
        ItemMeta meta = stack.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(name);
        stack.setItemMeta(meta);
    }
    public static void setLore(ItemStack stack, String lore) {
        ItemMeta meta = stack.getItemMeta();
        Objects.requireNonNull(meta).setLore(Collections.singletonList(lore));
        stack.setItemMeta(meta);
    }
    public static boolean checkDisplayName(ItemStack e, String title){
        return Objects.requireNonNull(e.getItemMeta()).getDisplayName().equals(title);
    }
    public static boolean checkLore(ItemStack e, String lore){
        return Objects.equals(Objects.requireNonNull(e.getItemMeta()).getLore(), Collections.singletonList(lore));
    }


}
