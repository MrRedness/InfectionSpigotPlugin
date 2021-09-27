package me.mrredness.infection.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.mrredness.infection.BorderUtils;
import me.mrredness.infection.commands.DataHelper;


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
                    p.sendMessage(ChatColor.GREEN + "Ok, border setup is complete! Test it using the setup menu.");
                } else if (m.equalsIgnoreCase("no") || m.equalsIgnoreCase("n")) {
                    DataHelper.addAndSave("Infection Physical Border", false);
                    PlayerInteractListener.readyForPlayerInputOnPhysicalBorder = false;
                    p.sendMessage(ChatColor.GREEN + "Ok, border setup is complete! Test it using the setup menu.");
                } else {
                    p.sendMessage(ChatColor.RED +"Please use either yes or no.");
                }

            }
        }
        if (ContainerListener.readyForPlayerInputOnDisablingBorder) {
            Player p = e.getPlayer();
            String m = e.getMessage();
            e.setCancelled(true);
            if (p.equals(ContainerListener.user)) {
                if (m.equalsIgnoreCase("end") || m.equalsIgnoreCase("e")) {
                    ContainerListener.readyForPlayerInputOnDisablingBorder = false;
                    BorderUtils.removeBorder();
                    p.sendMessage(ChatColor.GREEN + "Ok, border is down!");
                }

            }
        }
    }
    
}
