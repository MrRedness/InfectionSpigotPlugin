package me.mrredness.infection.listeners;

import me.mrredness.infection.commands.DataHelper;
import me.mrredness.infection.commands.RangeHelper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;

public class PlayerInteractListener implements Listener {

    static boolean atLeastOneBorderPositionSet = false;
    static String eTitle;
    static boolean readyForPlayerInputOnPhysicalBorder = false;
    static Player setupUser;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            try {
                eTitle = e.getItem().getItemMeta().getDisplayName();
            }
            catch (NullPointerException exception) {eTitle = "";}
            if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                if (eTitle.equals(ChatColor.DARK_AQUA + "Spawn Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    Location clickBlock = e.getClickedBlock().getLocation().add(0, 1, 0);
                    String SpawnCoords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                    String SpawnWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                    p.sendMessage(ChatColor.AQUA + "The spawn location for infection has been set to " + ChatColor.RED + SpawnCoords + ChatColor.AQUA + " in the " + ChatColor.RED + SpawnWorld + ".");
                    DataHelper.addAndSave("Infection Spawn Coordinates", SpawnCoords);
                    DataHelper.addAndSave("Infection Spawn World", SpawnWorld);
                    DataHelper.addAndSave("Infection Spawn Location", clickBlock);
                    DataHelper.addAndSave("Infection Spawn Setup Complete", false);
                    p.getInventory().remove(e.getItem());
                }
                if (eTitle.equals(ChatColor.GREEN + "Border Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    Location clickBlock = e.getClickedBlock().getLocation();
                    String pos1Coords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                    String BorderWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                    if (!DataHelper.check("Infection Spawn World", BorderWorld)) {
                        p.sendMessage(ChatColor.RED + "The border must be in the same world as the spawn location.");
                    }
                    else {
                        p.sendMessage(ChatColor.GREEN + "The pos1 border location for infection has been set to " + ChatColor.RED + pos1Coords + ChatColor.GREEN + " in the " + ChatColor.RED + BorderWorld + ".");
                        DataHelper.addAndSave("Infection Border pos1 Coordinates", pos1Coords);
                        DataHelper.addAndSave("Infection Border pos1 Location", clickBlock);
                        p.sendMessage(ChatColor.GOLD + "Go to the the other corner of your border and right click");
                        atLeastOneBorderPositionSet = true;
                    }
                }
            } else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                if (eTitle.equals(ChatColor.GREEN + "Border Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    if (!atLeastOneBorderPositionSet) {
                        p.sendMessage(ChatColor.RED + "Set position 1 first by left clicking on the desired block.");
                    } else {
                        Location clickBlock = e.getClickedBlock().getLocation();
                        String BorderWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                        Location otherBorderBlock = (Location) DataHelper.get("Infection Border pos1 Location");
                        Location spawnBlock = (Location) DataHelper.get("Infection Spawn Location");
                        if (!DataHelper.check("Infection Spawn World", BorderWorld)) {
                            p.sendMessage(ChatColor.RED + "Pos2 must be in the same world as pos1.");
                        } else if (!RangeHelper.isSpawnInLocationRange(clickBlock, otherBorderBlock, spawnBlock)) {
                            p.sendMessage(ChatColor.RED + "Your border must contain your spawn location inside of it.");
                        }
                        else {
                            String pos2Coords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                            p.sendMessage(ChatColor.GREEN + "The pos2 border location for infection has been set to " + ChatColor.RED + pos2Coords + ChatColor.GREEN + " in the " + ChatColor.RED + BorderWorld + ".");
                            DataHelper.addAndSave("Infection Border pos2 Coordinates", pos2Coords);
                            DataHelper.addAndSave("Infection Border pos2 Location", clickBlock);
                            HashMap<String, Integer> range = RangeHelper.createCoordinateRange(clickBlock,otherBorderBlock);
                            DataHelper.addAndSave("Infection Border Range", range);
                            p.getInventory().remove(e.getItem());
                            atLeastOneBorderPositionSet = false;
                            p.sendMessage(ChatColor.DARK_PURPLE + "Finally, choose if you would like to setup a physical border around the box you just set.");
                            p.sendMessage(ChatColor.DARK_PURPLE + "This depends on the plugin \"World Border 1.15+\".");
                            p.sendMessage(ChatColor.DARK_PURPLE + "Otherwise, the plugin will only teleport players inside the border and assume that players cannot get out on their own.");
                            p.sendMessage(ChatColor.DARK_AQUA + "If you would like this, please type \"Yes\". Otherwise, type \"No\" in the chat.");
                            p.sendMessage(ChatColor.RED + "If no option is set, the plugin assumes that you would like this feature enabled.");

                            readyForPlayerInputOnPhysicalBorder = true;
                            setupUser = p;
                        }
                    }
                }
            }
        }
    }
}
