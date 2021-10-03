package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionGame;
import me.mrredness.helpers.DataHelper;
import me.mrredness.helpers.MetaHelper;
import me.mrredness.helpers.RangeHelper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;

public class PlayerInteractListener implements Listener {

    static boolean atLeastOneBorderPositionSet = false;
    static String eTitle;
    static boolean readyForPlayerInputOnPhysicalBorder = false;
    static boolean readyForPlayerInputOnLobbyBorder = false;
    static boolean readyForPlayerToSetLobbyBorder = false;
    static boolean atLeastOneLobbyBorderPositionSet = false;
    static Player setupUser;
    static ItemStack setupItem;
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (Objects.requireNonNull(e.getItem()).getType().equals(Material.MAP)) {
            try {
                eTitle = e.getItem().getItemMeta().getDisplayName();
            }
            catch (NullPointerException exception) {eTitle = "";}
            if (eTitle.equals(ChatColor.AQUA + "Choose your role in Infection!")) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                Inventory chooseRoleInv = Bukkit.createInventory(p, 9, ChatColor.AQUA + "Choose your role in Infection!");
                ItemStack infected = new ItemStack(Material.DIAMOND_SWORD, 1);
                MetaHelper.setDisplayName(infected, ChatColor.RED + "Infected");
                String numberOfInfected = String.valueOf(InfectionGame.getInfected().size());
                if (numberOfInfected.equals("1")) {MetaHelper.setLore(infected, (ChatColor.BLUE + "1 player"));}
                else {MetaHelper.setLore(infected, (ChatColor.BLUE + numberOfInfected + " players"));}
                ItemStack hider = new ItemStack(Material.FEATHER, 1);
                MetaHelper.setDisplayName(hider, ChatColor.GREEN + "Hider");
                String numberOfHiders = String.valueOf(InfectionGame.getHiders().size());
                if (numberOfHiders.equals("1")) {MetaHelper.setLore(hider, (ChatColor.BLUE + "1 player"));}
                else {MetaHelper.setLore(hider, (ChatColor.BLUE + numberOfHiders + " players"));}
                ItemStack random = new ItemStack(Material.ENCHANTED_BOOK, 1);
                MetaHelper.setDisplayName(random, ChatColor.BLUE + "Random Role");
                String numberOfRandom = String.valueOf(InfectionGame.getChosenRandom().size());
                if (numberOfRandom.equals("1")) {MetaHelper.setLore(random, (ChatColor.DARK_PURPLE + "1 player"));}
                else {MetaHelper.setLore(random, (ChatColor.DARK_PURPLE + numberOfRandom + " players"));}
                chooseRoleInv.setItem(1, infected);
                chooseRoleInv.setItem(4, hider);
                chooseRoleInv.setItem(7, random);
                p.openInventory(chooseRoleInv);
            }
        }
        else if (e.getClickedBlock() != null) {
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
                    DataHelper.addAndSave("Infection Spawn Setup Complete", true);
                    p.getInventory().remove(e.getItem());
                }
                else if (eTitle.equals(ChatColor.AQUA + "Lobby Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    Location clickBlock = e.getClickedBlock().getLocation();
                    String clickCoords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                    String clickWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                    if (readyForPlayerToSetLobbyBorder) {
                        if (!DataHelper.check("Infection Lobby World", clickWorld)) {
                            p.sendMessage(ChatColor.RED + "The lobby border must be in the same world as the lobby spawn location.");
                        }
                        else {
                            p.sendMessage(ChatColor.GREEN + "The pos1 border location for the lobby has been set to " + ChatColor.RED + clickCoords + ChatColor.GREEN + " in the " + ChatColor.RED + clickWorld + ".");
                            DataHelper.addAndSave("Infection Lobby Border pos1 Coordinates", clickCoords);
                            DataHelper.addAndSave("Infection Lobby Border pos1 Location", clickBlock);
                            p.sendMessage(ChatColor.GOLD + "Go to the the other corner of your border and right click");
                            atLeastOneLobbyBorderPositionSet = true;
                        }
                    }
                    else {
                        p.sendMessage(ChatColor.LIGHT_PURPLE + "The spawn location for the lobby has been set to " + ChatColor.RED + clickCoords + ChatColor.LIGHT_PURPLE + " in the " + ChatColor.RED + clickWorld + ".");
                        DataHelper.addAndSave("Infection Lobby Spawn Coordinates", clickCoords);
                        DataHelper.addAndSave("Infection Lobby World", clickWorld);
                        DataHelper.addAndSave("Infection Lobby Spawn Location", clickBlock);
                        p.sendMessage(ChatColor.DARK_GREEN + "Would you would like to setup a border around the lobby?");
                        p.sendMessage(ChatColor.DARK_GREEN + "This depends on the plugin \"World Border 1.15+\".");
                        p.sendMessage(ChatColor.DARK_GREEN + "Otherwise, the plugin will only teleport players to the lobby spawn and assume that players cannot get out on their own.");
                        p.sendMessage(ChatColor.DARK_AQUA + "If you would like this, please type \"Yes\". Otherwise, type \"No\" in the chat.");
                        readyForPlayerInputOnLobbyBorder = true;
                        setupUser = p;
                        setupItem = e.getItem();
                    }
                }
                else if (eTitle.equals(ChatColor.GREEN + "Border Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    Location clickBlock = e.getClickedBlock().getLocation();
                    String pos1Coords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                    String BorderWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                    if (!DataHelper.checkBoolean("Infection Spawn Setup Complete")) {
                        p.sendMessage(ChatColor.RED + "Please setup spawn first.");
                    }
                    else if (!DataHelper.check("Infection Spawn World", BorderWorld)) {
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
                            p.sendMessage(ChatColor.RED + "Pos2 must be in the same world as pos1 & the lobby spawn.");
                        } else if (!RangeHelper.isSpawnInLocationRange(clickBlock, otherBorderBlock, spawnBlock)) {
                            p.sendMessage(ChatColor.RED + "Your border must contain your lobby spawn location inside of it.");
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
                            p.sendMessage(ChatColor.DARK_PURPLE + "Finally, choose if you would like to setup a border around the box you just set.");
                            p.sendMessage(ChatColor.DARK_PURPLE + "This depends on the plugin \"World Border 1.15+\".");
                            p.sendMessage(ChatColor.DARK_PURPLE + "Otherwise, the plugin will only teleport players inside the border and assume that players cannot get out on their own.");
                            p.sendMessage(ChatColor.DARK_AQUA + "If you would like this, please type \"Yes\". Otherwise, type \"No\" in the chat.");

                            readyForPlayerInputOnPhysicalBorder = true;
                            setupUser = p;
                        }
                    }
                }
                if (eTitle.equals(ChatColor.AQUA + "Lobby Coordinate Picker")) {
                    e.setCancelled(true);
                    Player p = e.getPlayer();
                    if (!atLeastOneLobbyBorderPositionSet) {
                        p.sendMessage(ChatColor.RED + "Set position 1 first by left clicking on the desired block.");
                    } else {
                        Location clickBlock = e.getClickedBlock().getLocation().add(0, 1, 0);
                        String BorderWorld = Objects.requireNonNull(clickBlock.getWorld()).getName();
                        Location otherBorderBlock = (Location) DataHelper.get("Infection Lobby Border pos1 Location");
                        Location spawnBlock = (Location) DataHelper.get("Infection Lobby Spawn Location");
                        if (!DataHelper.check("Infection Lobby World", BorderWorld)) {
                            p.sendMessage(ChatColor.RED + "Pos2 must be in the same world as pos1.");
                        } else if (!RangeHelper.isSpawnInLocationRange(clickBlock, otherBorderBlock, spawnBlock)) {
                            p.sendMessage(ChatColor.RED + "Your border must contain your spawn location inside of it.");
                        }
                        else {
                            String pos2Coords = clickBlock.getBlockX() + ", " + clickBlock.getBlockY() + ", " + clickBlock.getBlockZ();
                            p.sendMessage(ChatColor.GREEN + "The pos2 lobby border location for infection has been set to " + ChatColor.RED + pos2Coords + ChatColor.GREEN + " in the " + ChatColor.RED + BorderWorld + ".");
                            DataHelper.addAndSave("Infection Lobby Border pos2 Coordinates", pos2Coords);
                            DataHelper.addAndSave("Infection Lobby Border pos2 Location", clickBlock);
                            HashMap<String, Integer> range = RangeHelper.createCoordinateRange(clickBlock,otherBorderBlock);
                            DataHelper.addAndSave("Infection Lobby Border Range", range);
                            p.getInventory().remove(e.getItem());
                            atLeastOneLobbyBorderPositionSet = false;
                            readyForPlayerToSetLobbyBorder = false;
                        }
                    }
                }
            }
        }
    }
}
