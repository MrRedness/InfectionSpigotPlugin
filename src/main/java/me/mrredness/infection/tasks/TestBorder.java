package me.mrredness.infection.tasks;

import me.mrredness.infection.helpers.DataHelper;
import me.mrredness.infection.listeners.ContainerListener;
import me.mrredness.infection.tasks.AsyncToSync.PlayerTeleport;
import me.mrredness.infection.utils.BorderUtils;
import me.mrredness.infection.utils.SleepUtils;
import me.mrredness.infection.utils.TeleportUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class TestBorder extends BukkitRunnable {

    Player p;
    HashMap<String, Integer> range;

    public TestBorder(Player p, HashMap<String, Integer> range) {
        this.p = p;
        this.range = range;
    }

    @Override
    public void run() {
        for (int a = 0; a < 5; a++) {
            new PlayerTeleport(p, TeleportUtils.findSafeLocation(range)).runTask(Bukkit.getPluginManager().getPlugin("Infection"));
            SleepUtils.three();
        }
        if (DataHelper.checkBoolean("Infection Physical Border")) {
            p.sendMessage(ChatColor.GOLD + "The plugin will now attempt to setup a physical border around the coordinates you set. If you do not want this, please redo border setup and choose \"No\" when asked about wanting a physical border.");
            if (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
                BorderUtils.setBorder("Game");
                p.sendMessage(ChatColor.GOLD + "The border should now be setup. Walk around and make sure it is working. When you are done, type \"end\" in chat to disable the border.");
                ContainerListener.setUser(p);
                ContainerListener.setReadyForPlayerInputOnDisablingTestBorder(true);
            } else {
                p.sendMessage(ChatColor.RED + "It seems the plugin \"World Border 1.15+\" is not installed. This plugin is optional for the core functionalities of Infection, but is required for the physical border. If you would like to disable the physical border, re-do the border setup. Otherwise, install \"World Border 1.15+\" from spigot.org.");
            }
        }
    }
}
