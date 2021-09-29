package me.mrredness.infection.commands;

import me.mrredness.infection.Infection;
import me.mrredness.infection.InfectionGameUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class InfectionCommand implements CommandExecutor {

    private final Infection plugin;
    private final boolean worldBorderEnabled;

    public InfectionCommand(Infection plugin, boolean worldBorderEnabled) {
        this.plugin = plugin;
        this.worldBorderEnabled = worldBorderEnabled;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player p) {
            if(args.length == 0) {return false;}
            else if (args[0].equals("join")) {
            /*    Inventory joinMenu = Bukkit.createInventory(p, 9, "Join Infection!");
                //    plugin.logger.log(new LogRecord(Level.INFO, String.valueOf(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")))));
                ItemStack joinStack = new ItemStack(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")), 1);
                ItemMeta joinStack_meta = joinStack.getItemMeta();
                Objects.requireNonNull(joinStack_meta).setDisplayName(ChatColor.BLUE + "Join Infection!");
                joinStack.setItemMeta(joinStack_meta);
                joinMenu.addItem(joinStack);
                p.openInventory(joinMenu);
            */

                // insert command to teleport to arena
                if (!(DataHelper.checkBoolean("Infection Border Setup Complete") || DataHelper.checkBoolean("Infection Spawn Setup Complete") || DataHelper.checkBoolean("Infection Lobby Setup Complete") || DataHelper.checkBoolean("Infection Options Setup Complete"))) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the border and spawn using the '/infection setup' menu.");
                }
                else if (!DataHelper.checkBoolean("Infection Border Setup Complete")) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the border using the 'Setup Infection Border' item in the '/infection setup' menu.");
                }
                else if (!DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the infected spawn location using the 'Setup Infected Spawn Coordinates' item in the '/infection setup' menu.");
                }
                else if (!DataHelper.checkBoolean("Infection Lobby Setup Complete")) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the lobby using the 'Setup Lobby' item in the '/infection setup' menu.");
                }
                else {
                    InfectionGameUtils.joinGame(p);
                }

                return true;
            }
            else if (args[0].equals("setup")) {
                Inventory setupMenu = Bukkit.createInventory(p, 9, "Setup Infection!");
                //    plugin.logger.log(new LogRecord(Level.INFO, String.valueOf(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")))));
                ItemStack setupSpawn = new ItemStack(Material.RED_BED, 1);
                MetaHelper.setDisplayName(setupSpawn, ChatColor.GOLD + "Setup Infected Spawn Coordinates");
                ItemStack setupBorder = new ItemStack(Material.BARRIER, 1);
                MetaHelper.setDisplayName(setupBorder, ChatColor.GREEN + "Setup Infection Border");
                ItemStack testBorder = new ItemStack(Material.ENDER_PEARL);
                MetaHelper.setDisplayName(testBorder, ChatColor.BLUE + "Test Border");
                ItemStack setupLobby = new ItemStack(Material.CLOCK);
                MetaHelper.setDisplayName(setupLobby, ChatColor.AQUA + "Setup Lobby");
                ItemStack setOptions = new ItemStack(Material.WRITABLE_BOOK);
                MetaHelper.setDisplayName(setOptions, ChatColor.DARK_PURPLE + "Set Options");
                setupMenu.addItem(setupSpawn, setupBorder, testBorder, setupLobby, setOptions);
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
