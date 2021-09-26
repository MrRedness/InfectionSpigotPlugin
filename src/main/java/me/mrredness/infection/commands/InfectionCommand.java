package me.mrredness.infection.commands;

import me.mrredness.infection.Infection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class InfectionCommand implements CommandExecutor {

    private final Infection plugin;

    public InfectionCommand(Infection plugin) {this.plugin = plugin;}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if(args.length == 0) {return false;}
            else if (args[0].equals("join")) {
                Inventory joinMenu = Bukkit.createInventory(p, 9, "Join Infection!");
                //    plugin.logger.log(new LogRecord(Level.INFO, String.valueOf(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")))));
                ItemStack joinStack = new ItemStack(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")), 1);
                ItemMeta joinStack_meta = joinStack.getItemMeta();
                Objects.requireNonNull(joinStack_meta).setDisplayName(ChatColor.BLUE + "Join Infection!");
                joinStack.setItemMeta(joinStack_meta);
                joinMenu.addItem(joinStack);
                p.openInventory(joinMenu);
                return true;
            }
            else if (args[0].equals("setup")) {
                Inventory setupMenu = Bukkit.createInventory(p, 9, "Setup Infection!");
                //    plugin.logger.log(new LogRecord(Level.INFO, String.valueOf(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")))));
                ItemStack setupSpawn = new ItemStack(Material.RED_BED, 1);
                MetaHelper.setDisplayName(setupSpawn, ChatColor.GOLD + "Setup Spawn Coordinates");
                ItemStack setupBorder = new ItemStack(Material.BARRIER, 1);
                MetaHelper.setDisplayName(setupBorder, ChatColor.GREEN + "Setup Infection Border");
                ItemStack testBorder = new ItemStack(Material.ENDER_PEARL);
                MetaHelper.setDisplayName(testBorder, ChatColor.BLUE + "Test Border");
                setupMenu.addItem(setupSpawn, setupBorder, testBorder);
                p.openInventory(setupMenu);
                return true;
            }
            else {return false;}
        } else {
            LogRecord console = new LogRecord(Level.WARNING, "This command can only be run by players.");
            plugin.logger.log(console);
        }
        return false;
    }
}
