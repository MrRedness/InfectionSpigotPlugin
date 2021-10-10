package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGame;
import me.mrredness.infection.helpers.DataHelper;
import me.mrredness.infection.helpers.MetaHelper;
import me.mrredness.infection.tasks.TestBorder;
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

    public static void setReadyForPlayerInputOnDisablingTestBorder(boolean readyForPlayerInputOnDisablingTestBorder) {
        ContainerListener.readyForPlayerInputOnDisablingTestBorder = readyForPlayerInputOnDisablingTestBorder;
    }

    static Player user;

    public static void setUser(Player user) {
        ContainerListener.user = user;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
   /*     else if (e.getView().getTitle().equals("Join Infection!")) {
            e.setCancelled(true);
            if (MetaHelper.checkDisplayName(i,(ChatColor.BLUE + "Join Infection!")) {
                p.sendMessage("You have joined Infection!");
            }
        }
     */
        if (e.getCurrentItem() != null) {
            if (e.getView().getTitle().equals(ChatColor.AQUA + "Choose your role in Infection!")) {
                e.setCancelled(true);
                ItemStack i = e.getCurrentItem();
                double numberOfPlayers = Math.max(InfectionGame.getMinNumberOfPlayers(), InfectionGame.getPlayersInGame().size());
                if (MetaHelper.checkDisplayName(i, ChatColor.RED + "Infected")) {
                    if (((InfectionGame.getInfected().size() + 1) / numberOfPlayers) <= 0.5) {
                        InfectionGame.addToInfected(p);
                    } else {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, there are too many infected players. Please choose another role.");
                    }
                    p.closeInventory();
                } else if (MetaHelper.checkDisplayName(i, ChatColor.GREEN + "Hider")) {
                    if (((InfectionGame.getHiders().size() + 1) / numberOfPlayers) <= 0.7) {
                        InfectionGame.addToHider(p);
                    } else {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "Sorry, there are too many hiders. Please choose another role.");
                    }
                    p.closeInventory();
                } else if (MetaHelper.checkDisplayName(i, ChatColor.BLUE + "Random Role")) {
                    InfectionGame.addToRandom(p);
                    p.closeInventory();
                }
            } else if (e.getView().getTitle().equals("Setup Infection!")) {
                e.setCancelled(true);
                ItemStack i = e.getCurrentItem();
                if (MetaHelper.checkDisplayName(i, ChatColor.GOLD + "Setup Infected Spawn Coordinates")) {
                    Inventory pInv = p.getInventory();
                    ItemStack coordinatePicker = new ItemStack(Material.GOLDEN_AXE, 1);
                    MetaHelper.setDisplayName(coordinatePicker, ChatColor.DARK_AQUA + "Spawn Coordinate Picker");
                    if (!pInv.addItem(coordinatePicker).isEmpty()) {
                        p.sendMessage("Please have at least one inventory slot open.");
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as the spawn for the infected (uninfected will be randomly teleported around the border), then left click (using the coordinate picker) on the block directly beneath the spawn coords.");
                        DataHelper.addAndSave("Infection Spawn Setup Complete", false);
                    }
                    p.closeInventory();
                } else if (MetaHelper.checkDisplayName(i, ChatColor.GREEN + "Setup Infection Border")) {

                    Inventory pInv = p.getInventory();
                    ItemStack coordinatePicker = new ItemStack(Material.WOODEN_AXE, 1);
                    MetaHelper.setDisplayName(coordinatePicker, ChatColor.GREEN + "Border Coordinate Picker");
                    if (!pInv.addItem(coordinatePicker).isEmpty()) {
                        p.sendMessage("Please have at least one inventory slot open.");
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Go to the first corner of your border and left click (using the coordinate picker).");
                        DataHelper.addAndSave("Infection Border Setup Complete", false);
                    }
                    p.closeInventory();
                } else if (MetaHelper.checkDisplayName(i, ChatColor.BLUE + "Test Border")) {
                    if (!(DataHelper.checkBoolean("Infection Border Setup Complete") || DataHelper.checkBoolean("Infection Spawn Setup Complete"))) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the border and spawn using the '/infection setup' menu.");
                    } else if (!DataHelper.checkBoolean("Infection Border Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the border using the 'Setup Infection Border' item in the '/infection setup' menu.");
                    } else if (!DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please finish setting up the infected spawn location using the 'Setup Infected Spawn Coordinates' item in the '/infection setup' menu.");
                    } else {
                        p.sendMessage(ChatColor.DARK_AQUA + "You will now be randomly teleported a few times within the border you set. If you are teleported outside your set bounds, something is wrong.");
                        HashMap<String, Integer> range = DataHelper.getHashMap("Infection Border Range");
                        if (range == null) {
                            p.sendMessage("Something went wrong. Try re-running the border setup.");
                        } else {
                            new TestBorder(p, range).runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Infection"));
                        }
                    }
                } else if (MetaHelper.checkDisplayName(i, ChatColor.AQUA + "Setup Lobby")) {
                    Inventory pInv = p.getInventory();
                    ItemStack coordinatePicker = new ItemStack(Material.STONE_AXE, 1);
                    MetaHelper.setDisplayName(coordinatePicker, ChatColor.AQUA + "Lobby Coordinate Picker");
                    if (!pInv.addItem(coordinatePicker).isEmpty()) {
                        p.sendMessage("Please have at least one inventory slot open.");
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as your lobby's spawn, then left click (using the coordinate picker) on the block underneath the spawn location.");
                        DataHelper.addAndSave("Infection Lobby Setup Complete", false);
                    }
                    p.closeInventory();
                } else if (MetaHelper.checkDisplayName(i, ChatColor.DARK_PURPLE + "Set Options")) {
                    e.setCancelled(true);
                    DataHelper.addIfDoesNotExist("Min Number of Players", 2);
                    DataHelper.addIfDoesNotExist("Max Number of Players", 10);
                    DataHelper.addIfDoesNotExist("Allow Choice of Role", true);
                    DataHelper.addIfDoesNotExist("Infection Options Setup Complete", true);

                    InfectionGame.setMinNumberOfPlayers((Integer) DataHelper.get("Min Number of Players"));
                    InfectionGame.setMaxNumberOfPlayers((Integer) DataHelper.get("Max Number of Players"));

                    Inventory optionsMenu = Bukkit.createInventory(p, 9, ChatColor.DARK_PURPLE + "Set Infection Options!");
                    ItemStack minNumberOfPlayers = new ItemStack(Material.WRITABLE_BOOK, (Integer) DataHelper.get("Min Number of Players"));
                    MetaHelper.setDisplayName(minNumberOfPlayers, ChatColor.GOLD + "Minimum Number of Players");
                    MetaHelper.setLore(minNumberOfPlayers, ChatColor.AQUA + "(left click to decrease, right click to increase)");
                    ItemStack maxNumberOfPlayers = new ItemStack(Material.WRITABLE_BOOK, (Integer) DataHelper.get("Max Number of Players"));
                    MetaHelper.setDisplayName(maxNumberOfPlayers, ChatColor.GOLD + "Maximum Number of Players");
                    MetaHelper.setLore(maxNumberOfPlayers, ChatColor.AQUA + "(left click to decrease, right click to increase)");
                    ItemStack allowChoice = new ItemStack(Material.WRITABLE_BOOK, 1);
                    MetaHelper.setDisplayName(allowChoice, ChatColor.GOLD + "Allow players to choose their role (infected or hider)?");
                    if (DataHelper.checkBoolean("Allow Choice of Role")) {
                        MetaHelper.setLore(allowChoice, ChatColor.GREEN + "Currently Set to Yes");
                    } else {
                        MetaHelper.setLore(allowChoice, ChatColor.RED + "Currently Set to No");
                    }
                    ItemStack save = new ItemStack(Material.LEVER, 1);
                    MetaHelper.setDisplayName(save, ChatColor.GOLD + "Click to save!");
                    MetaHelper.setLore(save, ChatColor.BLUE + "You can still change these options later.");
                    optionsMenu.addItem(minNumberOfPlayers, maxNumberOfPlayers, allowChoice);
                    optionsMenu.setItem(8, save);
                    p.openInventory(optionsMenu);
                }
            } else if (e.getView().getTitle().equals(ChatColor.DARK_PURPLE + "Set Infection Options!")) {
                e.setCancelled(true);
                ItemStack i = e.getCurrentItem();
                if (MetaHelper.checkDisplayName(i, ChatColor.GOLD + "Minimum Number of Players")) {
                    int amount = i.getAmount();
                    int amountOfMax = Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(1)).getAmount();
                    if (amount > 2 && e.getClick().isLeftClick()) {
                        i.setAmount(amount - 1);
                    } else if (amount < amountOfMax && e.getClick().isRightClick()) {
                        i.setAmount(amount + 1);
                    }
                    DataHelper.addAndSave("Min Number of Players", i.getAmount());
                    InfectionGame.setMinNumberOfPlayers(i.getAmount());
                } else if (MetaHelper.checkDisplayName(i, ChatColor.GOLD + "Maximum Number of Players")) {
                    int amount = i.getAmount();
                    int amountOfMin = Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(0)).getAmount();
                    if (amount > amountOfMin && e.getClick().isLeftClick()) {
                        i.setAmount(amount - 1);
                    } else if (amount < 20 && e.getClick().isRightClick()) {
                        i.setAmount(amount + 1);
                    }
                    DataHelper.addAndSave("Max Number of Players", Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(1)).getAmount());
                    InfectionGame.setMaxNumberOfPlayers(i.getAmount());
                } else if (MetaHelper.checkDisplayName(i, ChatColor.GOLD + "Allow players to choose their role (infected or hider)?")) {
                    if (MetaHelper.checkLore(i, ChatColor.RED + "Currently Set to No")) {
                        MetaHelper.setLore(i, ChatColor.GREEN + "Currently Set to Yes");
                    } else {
                        MetaHelper.setLore(i, ChatColor.RED + "Currently Set to No");
                    }
                    DataHelper.addAndSave("Allow Choice of Role", !MetaHelper.checkLore(i, ChatColor.RED + "Currently Set to No"));
                } else if (MetaHelper.checkDisplayName(i, ChatColor.GOLD + "Click to save!")) {
                    DataHelper.addAndSave("Min Number of Players", Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(0)).getAmount());
                    DataHelper.addAndSave("Max Number of Players", Objects.requireNonNull(p.getOpenInventory().getTopInventory().getItem(1)).getAmount());
                    DataHelper.addAndSave("Allow Choice of Role", !MetaHelper.checkLore(i, ChatColor.RED + "Currently Set to No"));
                    InfectionGame.setMinNumberOfPlayers((Integer) DataHelper.get("Min Number of Players"));
                    InfectionGame.setMaxNumberOfPlayers((Integer) DataHelper.get("Max Number of Players"));
                    p.closeInventory();
                }
            }
        }
    }
}
