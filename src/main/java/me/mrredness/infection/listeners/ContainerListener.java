package me.mrredness.infection.listeners;

import me.mrredness.infection.BorderUtils;
import me.mrredness.infection.SleepUtils;
import me.mrredness.infection.TeleportUtils;
import me.mrredness.infection.commands.DataHelper;
import me.mrredness.infection.commands.MetaHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class ContainerListener implements Listener {
  //  private final Infection plugin;

  //  public ContainerListener(Infection plugin) {
  //      this.plugin = plugin;
  //  }
  static boolean readyForPlayerInputOnDisablingBorder = false;
  static Player user;

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) {}
        else if (e.getView().getTitle().equals("Join Infection!")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.BLUE + "Join Infection!")) {
                p.sendMessage("You have joined Infection!");
            }
        }
        else if (e.getView().getTitle().equals("Setup Infection!")) {
            e.setCancelled(true);
            if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.GOLD + "Setup Infected Spawn Coordinates")) {
                Inventory pInv = p.getInventory();
                ItemStack coordinatePicker = new ItemStack(Material.GOLDEN_AXE, 1);
                MetaHelper.setDisplayName(coordinatePicker, ChatColor.DARK_AQUA + "Spawn Coordinate Picker");
                if (!pInv.addItem(coordinatePicker).isEmpty()) {
                    p.sendMessage("Please have at least one inventory slot open.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as the spawn for the infected (uninfected will be randomly teleported around the border), then left click on the block directly beneath the spawn coords.");
                    DataHelper.addAndSave("Infection Spawn Setup Complete", false);
                    p.closeInventory();
                }
            }
                else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.GREEN + "Setup Infection Border")) {

                    Inventory pInv = p.getInventory();
                    ItemStack coordinatePicker = new ItemStack(Material.WOODEN_AXE, 1);
                    MetaHelper.setDisplayName(coordinatePicker, ChatColor.GREEN + "Border Coordinate Picker");
                    if (!pInv.addItem(coordinatePicker).isEmpty()) {
                        p.sendMessage("Please have at least one inventory slot open.");
                        p.closeInventory();
                    } else {
                        p.sendMessage(ChatColor.GOLD + "Go to the first corner of your border and left click");
                        DataHelper.addAndSave("Infection Border Setup Complete", false);
                        p.closeInventory();
                    }
            }
                else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.BLUE + "Test Border")) {
                    if (DataHelper.checkBoolean("Infection Border Setup Complete") || DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                        p.sendMessage(ChatColor.DARK_AQUA + "You will now be randomly teleported a few times within the border you set. If you are teleported outside your set bounds, something is wrong.");

                        for (int i = 0; i < 5; i++) {
                            p.teleport(TeleportUtils.findSafeLocation());
                            SleepUtils.three();
                        }
                        if (DataHelper.checkBoolean("Infection Physical Border")) {
                            p.sendMessage(ChatColor.GOLD + "The plugin will now attempt to setup a physical border around the coordinates you set. If you do not want this, please redo border setup and choose \"No\" when asked about wanting a physical border.");
                            p.sendMessage(ChatColor.RED + "This will fail if the plugin \"World Border 1.15+\" is not installed.");
                            BorderUtils.setBorder();
                            p.sendMessage(ChatColor.GOLD + "The border should now be setup. Walk around and make sure it is working. When you are done, type \"end\" in chat to disable the border.");
                            user = p;
                            readyForPlayerInputOnDisablingBorder = true;
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.RED + "Please finish setting up border and spawn first.");
                    }
            }
        }
    }
}
