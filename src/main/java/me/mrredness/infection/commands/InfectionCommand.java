package me.mrredness.infection.commands;

import me.mrredness.infection.helpers.DataHelper;
import me.mrredness.infection.helpers.MetaHelper;
import me.mrredness.infection.tasks.async.LobbyBarCountdownTask;
import me.mrredness.infection.Infection;
import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.tasks.AsyncToSync.WaitForSecondAttemptTask;
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
    private static boolean secondTimeRunningForceStart = false;

    public static void setSecondTimeRunningForceStart(boolean secondTimeRunningForceStart) {
        InfectionCommand.secondTimeRunningForceStart = secondTimeRunningForceStart;
    }

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
                if (p.hasPermission("infection.joinGame")) {
                    if (!(DataHelper.checkBoolean("Infection Border Setup Complete") || DataHelper.checkBoolean("Infection Spawn Setup Complete") || DataHelper.checkBoolean("Infection Lobby Setup Complete") || DataHelper.checkBoolean("Infection Options Setup Complete"))) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the border and spawn using the '/infection setup' menu.");
                    } else if (!DataHelper.checkBoolean("Infection Border Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the border using the 'Setup Infection Border' item in the '/infection setup' menu.");
                    } else if (!DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the infected spawn location using the 'Setup Infected Spawn Coordinates' item in the '/infection setup' menu.");
                    } else if (!DataHelper.checkBoolean("Infection Lobby Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the lobby using the 'Setup Lobby' item in the '/infection setup' menu.");
                    } else {
                        InfectionGame.joinGame(p, worldBorderEnabled, plugin);
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have the permission: " + ChatColor.BLUE + "\"infection.joinGame\"");
                    return false;
                }

            }
            else if (args[0].equals("leave")) {
                if (p.hasPermission("infection.joinGame")) {
                    InfectionGame.leaveGame(p);
                    return true;
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have the permission: " + ChatColor.BLUE + "\"infection.joinGame\"");
                    return false;
                }
            }
            else if (args[0].equals("setup")) {
                if (p.hasPermission("infection.setup")) {
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
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have the permission: " + ChatColor.BLUE + "\"infection.setup\"");
                    return false;
                }
            }
            else if (args[0].equals("forcestart")) {
                if (p.hasPermission("infection.forceStart")) {
                    if (args.length == 2) {
                        if (!secondTimeRunningForceStart) {
                            if (InfectionGame.getPlayersInGame().size() < InfectionGame.getMinNumberOfPlayers()) {
                                p.sendMessage(ChatColor.RED + "It seems you have less players than the minimum amount. Are you sure you wish to start the game? If yes, then run the command again.");
                                secondTimeRunningForceStart = true;
                                new WaitForSecondAttemptTask().runTaskLater(plugin, 1200);
                                return true;
                            }
                        }
                        int numberToSet = Integer.parseInt(args[1]);
                        if (numberToSet > 0) {
                            LobbyBarCountdownTask.setNumberOfSecondsUntilStart(Integer.parseInt(args[1]));
                            LobbyBarCountdownTask.setForceStart(true);
                            return true;
                        }
                        p.sendMessage(ChatColor.RED + "Please use a natural number (greater than 0).");
                    } else if (args.length == 1) {
                        if (!secondTimeRunningForceStart) {
                            if (InfectionGame.getPlayersInGame().size() < InfectionGame.getMinNumberOfPlayers()) {
                                p.sendMessage(ChatColor.RED + "It seems you have less players than the minimum amount. Are you sure you wish to start the game? If yes, then run the command again.");
                                secondTimeRunningForceStart = true;
                                new WaitForSecondAttemptTask().runTaskLater(plugin, 1200);
                                return true;
                            }
                        }
                        LobbyBarCountdownTask.setNumberOfSecondsUntilStart(1);
                        LobbyBarCountdownTask.setForceStart(true);

                        return true;
                    }
                }
                else {
                    p.sendMessage(ChatColor.RED + "You do not have the permission: " + ChatColor.BLUE + "\"infection.forceStart\"");
                    return false;
                }
            }
            else if (args[0].equals("endgame")) {
                if (p.hasPermission("infection.endGame")) {
                    if (!InfectionGame.endGame(ChatColor.DARK_PURPLE + "The game has been ended by a moderator.")) {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, the game is not currently running.");
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.RED + "You do not have the permission: " + ChatColor.BLUE + "\"infection.endGame\"");
                    return false;
                }
            }
            else {return false;}
        } else {
            LogRecord console = new LogRecord(Level.WARNING, "This command can only be run by players.");
            plugin.logger.log(console);
        }
        return false;
    }
}
