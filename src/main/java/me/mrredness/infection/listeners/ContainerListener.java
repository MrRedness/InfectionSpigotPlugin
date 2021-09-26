package me.mrredness.infection.listeners;

// import me.mrredness.infection.Infection;
import me.mrredness.infection.TeleportUtils;
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

import static java.lang.Thread.sleep;

public class ContainerListener implements Listener {
  //  private final Infection plugin;

  //  public ContainerListener(Infection plugin) {
  //      this.plugin = plugin;
  //  }
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
            if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.GOLD + "Setup Spawn Coordinates")) {
                Inventory pInv = p.getInventory();
                ItemStack coordinatePicker = new ItemStack(Material.GOLDEN_AXE, 1);
                MetaHelper.setDisplayName(coordinatePicker, ChatColor.DARK_AQUA + "Spawn Coordinate Picker");
                if (!pInv.addItem(coordinatePicker).isEmpty()) {
                    p.sendMessage("Please have at least one inventory slot open.");
                    p.closeInventory();
                } else {
                    p.sendMessage(ChatColor.GOLD + "Go to the coordinates you would like to use as spawn, then left click on the block directly beneath the spawn coords.");
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
                        p.closeInventory();
                    }
            }
                else if (Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName().equals(ChatColor.BLUE + "Test Border")) {
                    p.sendMessage("You will now be teleported a few times to a location inside the border you set. If you are teleported outside your set bounds, something is wrong.");
                    
                    for (int i = 0; i < 5; i++) {
                        p.teleport(TeleportUtils.findSafeLocation());
                        try {
                            sleep(5000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        }
    }
}
