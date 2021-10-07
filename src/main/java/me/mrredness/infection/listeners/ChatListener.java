package me.mrredness.infection.listeners;

import me.mrredness.infection.InfectionSetupData;
import me.mrredness.infection.helpers.DataHelper;
import me.mrredness.infection.utils.BorderUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        if (PlayerInteractListener.readyForPlayerInputOnPhysicalBorder) {
            Player p = e.getPlayer();
            String m = e.getMessage();
            e.setCancelled(true);
            if (p.equals(PlayerInteractListener.setupUser)) {
                if (m.equalsIgnoreCase("yes") || m.equalsIgnoreCase("y")) {
                    DataHelper.addAndSave("Infection Physical Border", true);
                    PlayerInteractListener.readyForPlayerInputOnPhysicalBorder = false;
                    DataHelper.addAndSave("Infection Border Setup Complete", true);
                    p.sendMessage(ChatColor.GREEN + "Ok, border setup is complete! Test it using the setup menu.");
                    InfectionSetupData.save();
                    InfectionSetupData.reload();
                } else if (m.equalsIgnoreCase("no") || m.equalsIgnoreCase("n")) {
                    DataHelper.addAndSave("Infection Physical Border", false);
                    PlayerInteractListener.readyForPlayerInputOnPhysicalBorder = false;
                    DataHelper.addAndSave("Infection Border Setup Complete", true);
                    p.sendMessage(ChatColor.GREEN + "Ok, border setup is complete! Test it using the setup menu.");
                    InfectionSetupData.save();
                    InfectionSetupData.reload();
                } else {
                    p.sendMessage(ChatColor.RED + "Please use either yes or no.");
                }
            }
        } else if (PlayerInteractListener.readyForPlayerInputOnLobbyBorder) {
            Player p = e.getPlayer();
            String m = e.getMessage();
            e.setCancelled(true);
            if (p.equals(PlayerInteractListener.setupUser)) {
                if (m.equalsIgnoreCase("yes") || m.equalsIgnoreCase("y")) {
                    DataHelper.addAndSave("Infection Lobby Border", true);
                    PlayerInteractListener.readyForPlayerInputOnLobbyBorder = false;
                    PlayerInteractListener.readyForPlayerToSetLobbyBorder = true;
                    p.sendMessage("Ok. Now use your coordinate picker to left click on one of the corners of your lobby border.");
                } else if (m.equalsIgnoreCase("no") || m.equalsIgnoreCase("n")) {
                    DataHelper.addAndSave("Infection Lobby Border", false);
                    PlayerInteractListener.readyForPlayerInputOnLobbyBorder = false;
                    DataHelper.addAndSave("Infection Lobby Setup Complete", true);
                    p.sendMessage(ChatColor.GREEN + "Ok, lobby setup is complete!");
                    p.getInventory().remove(PlayerInteractListener.setupItem);
                } else {
                    p.sendMessage(ChatColor.RED + "Please use either yes or no.");
                }
            }
        } else if (ContainerListener.readyForPlayerInputOnDisablingTestBorder) {
            Player p = e.getPlayer();
            String m = e.getMessage();
            e.setCancelled(true);
            if (p.equals(ContainerListener.user)) {
                if (m.equalsIgnoreCase("end") || m.equalsIgnoreCase("e")) {
                    ContainerListener.readyForPlayerInputOnDisablingTestBorder = false;
                    BorderUtils.removeBorder("Game");
                    p.sendMessage(ChatColor.GREEN + "Ok, border is down!");
                }

            }
        }
    }

}
