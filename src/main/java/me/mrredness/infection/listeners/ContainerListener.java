package me.mrredness.infection.listeners;

import me.mrredness.infection.BorderUtils;
import me.mrredness.infection.SleepUtils;
import me.mrredness.infection.TeleportUtils;
import me.mrredness.infection.commands.DataHelper;
import me.mrredness.infection.commands.MetaHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;


public class ContainerListener implements Listener {
  //  private final Infection plugin;

    //  public ContainerListener(Infection plugin) {
    //      this.plugin = plugin;
    //  }
    static boolean readyForPlayerInputOnDisablingTestBorder = false;
    static Player user;
    boolean worldBorderEnabled;
    
    public ContainerListener(boolean worldBorderEnabled) {this.worldBorderEnabled = worldBorderEnabled;}

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) {}
   /*     else if (e.getView().getTitle().equals("Join Infection!")) {
            e.setCancelled(true);
            if (MetaHelper.checkDisplayName(i,(ChatColor.BLUE + "Join Infection!")) {
                p.sendMessage("You have joined Infection!");
            }
        }
     */   else if (e.getView().getTitle().equals("Setup Infection!")) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (MetaHelper.checkDisplayName(i,ChatColor.GOLD + "Setup Infected Spawn Coordinates")) {
                Inventory pInv = p.getInventory();
                ItemStack coordinatePicker = new ItemStack(Material.GOLDEN_AXE, 1);
                MetaHelper.setDisplayName(coordinatePicker, ChatColor.DARK_AQUA + "Spawn Coordinate Picker");
                if (!pInv.addItem(coordinatePicker).isEmpty()) {
                    p.sendMessage("Please have at least one inventory slot open.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as the spawn for the infected (uninfected will be randomly teleported around the border), then left click (using the coordinate picker) on the block directly beneath the spawn coords.");
                    DataHelper.addAndSave("Infection Spawn Setup Complete", false);
                    p.closeInventory();
                }
            }
            else if (MetaHelper.checkDisplayName(i,ChatColor.GREEN + "Setup Infection Border")) {

                    Inventory pInv = p.getInventory();
                    ItemStack coordinatePicker = new ItemStack(Material.WOODEN_AXE, 1);
                    MetaHelper.setDisplayName(coordinatePicker, ChatColor.GREEN + "Border Coordinate Picker");
                    if (!pInv.addItem(coordinatePicker).isEmpty()) {
                        p.sendMessage("Please have at least one inventory slot open.");
                        p.closeInventory();
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Go to the first corner of your border and left click (using the coordinate picker).");
                        DataHelper.addAndSave("Infection Border Setup Complete", false);
                        p.closeInventory();
                    }
            }
            else if (MetaHelper.checkDisplayName(i,ChatColor.BLUE + "Test Border")) {
                if (!(DataHelper.checkBoolean("Infection Border Setup Complete") || DataHelper.checkBoolean("Infection Spawn Setup Complete"))) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the border and spawn using the '/infection setup' menu.");
                }
                else if (!DataHelper.checkBoolean("Infection Border Setup Complete")) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the border using the 'Setup Infection Border' item in the '/infection setup' menu.");
                }
                else if (!DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                    p.sendMessage(ChatColor.RED + "Please finish setting up the infected spawn location using the 'Setup Infected Spawn Coordinates' item in the '/infection setup' menu.");
                }
                else {
                    p.sendMessage(ChatColor.DARK_AQUA + "You will now be randomly teleported a few times within the border you set. If you are teleported outside your set bounds, something is wrong.");
                    HashMap<String, Integer> range = DataHelper.getHashMap("Infection Border Range");
                    if (range == null) {
                        p.sendMessage("Something went wrong. Try re-running the border setup.");
                    }
                    else {
                        for (int a = 0; a < 5; a++) {
                            p.teleport(TeleportUtils.findSafeLocation(range));
                            SleepUtils.three();
                        }
                        if (DataHelper.checkBoolean("Infection Physical Border")) {
                            p.sendMessage(ChatColor.GOLD + "The plugin will now attempt to setup a physical border around the coordinates you set. If you do not want this, please redo border setup and choose \"No\" when asked about wanting a physical border.");
                            if (worldBorderEnabled) {
                                BorderUtils.setBorder("Infection Border Range", "Infection Spawn World");
                                p.sendMessage(ChatColor.GOLD + "The border should now be setup. Walk around and make sure it is working. When you are done, type \"end\" in chat to disable the border.");
                                user = p;
                                readyForPlayerInputOnDisablingTestBorder = true;
                            }
                            else {
                                p.sendMessage(ChatColor.RED + "It seems the plugin \"World Border 1.15+\" is not installed. This plugin is optional for the core functionalities of Infection, but is required for the physical border. If you would like to disable the physical border, re-do the border setup. Otherwise, install \"World Border 1.15+\" from spigot.org.");
                            }
                        }
                }
            }
                }
            else if (MetaHelper.checkDisplayName(i,ChatColor.AQUA + "Setup Lobby")) {
                Inventory pInv = p.getInventory();
                ItemStack coordinatePicker = new ItemStack(Material.STONE_AXE, 1);
                MetaHelper.setDisplayName(coordinatePicker, ChatColor.AQUA + "Lobby Coordinate Picker");
                if (!pInv.addItem(coordinatePicker).isEmpty()) {
                    p.sendMessage("Please have at least one inventory slot open.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as your lobby's spawn, then left click (using the coordinate picker) on the block underneath the spawn location.");
                    DataHelper.addAndSave("Infection Lobby Setup Complete", false);
                    p.closeInventory();
                }
            }
            else if (MetaHelper.checkDisplayName(i,ChatColor.DARK_PURPLE + "Set Options")) {
                e.setCancelled(true);
                Inventory optionsMenu = Bukkit.createInventory(p, 9, ChatColor.DARK_PURPLE + "Set Infection Options!");
                //    plugin.logger.log(new LogRecord(Level.INFO, String.valueOf(Material.valueOf(plugin.getConfig().getString("joinMenuJoinItem")))));
                ItemStack minNumberOfPlayers = new ItemStack(Material.WRITABLE_BOOK, 2);
                MetaHelper.setDisplayName(minNumberOfPlayers, ChatColor.GOLD + "Minimum Number of Players " + ChatColor.AQUA + "(left click to decrease, right click to increase)");
                ItemStack maxNumberOfPlayers = new ItemStack(Material.WRITABLE_BOOK, 4);
                MetaHelper.setDisplayName(maxNumberOfPlayers, ChatColor.GOLD + "Maximum Number of Players " + ChatColor.AQUA + "(left click to decrease, right click to increase)");
                ItemStack allowChoice = new ItemStack(Material.WRITABLE_BOOK, 1);
                MetaHelper.setDisplayName(allowChoice, ChatColor.GOLD + "Allow players to choose their role (infected or hider)?" + ChatColor.GREEN + "(left click for no and right click for yes)");
                MetaHelper.setLore(allowChoice,ChatColor.RED + "Currently Set to No");
                optionsMenu.addItem(minNumberOfPlayers, maxNumberOfPlayers, allowChoice);
                p.openInventory(optionsMenu);
            }
        }
        else if (e.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Set Infection Options!")) {
            e.setCancelled(true);
            ItemStack i = e.getCurrentItem();
            if (MetaHelper.checkDisplayName(i,ChatColor.GOLD + "Minimum Number of Players " + ChatColor.AQUA + "(left click to decrease, right click to increase)")) {
                int amount = i.getAmount();
                int amountOfMax =  Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(1)).getAmount();
                if (amount > 2 && e.getClick().isLeftClick()) {
                    i.setAmount(amount - 1);
                }
                else if (amount < amountOfMax && e.getClick().isRightClick()) {
                    i.setAmount(amount + 1);
                }
            }
            else if (MetaHelper.checkDisplayName(i,ChatColor.GOLD + "Maximum Number of Players " + ChatColor.AQUA + "(left click to decrease, right click to increase)")) {
                int amount = i.getAmount();
                int amountOfMin =  Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(0)).getAmount();
                if (amount > amountOfMin && e.getClick().isLeftClick()) {
                    i.setAmount(amount - 1);
                }
                else if (amount < 20 && e.getClick().isRightClick()) {
                    i.setAmount(amount + 1);
                }
            }
            else if (MetaHelper.checkDisplayName(i,ChatColor.GOLD + "Allow players to choose their role (infected or hider)?" + ChatColor.GREEN + "(left click for no and right click for yes)")) {
                if (MetaHelper.checkLore(i, ChatColor.RED + "Currently Set to No")) {
                    MetaHelper.setLore(i,ChatColor.GREEN + "Currently Set to Yes");
                }
                else {
                    MetaHelper.setLore(i,ChatColor.RED + "Currently Set to No");
                }
            }
        }
    }
}
