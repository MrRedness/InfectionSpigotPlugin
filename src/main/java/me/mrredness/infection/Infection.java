package me.mrredness.infection;

import me.mrredness.infection.commands.InfectionCommand;
import me.mrredness.infection.commands.InfectionTabCompletion;
import me.mrredness.infection.listeners.*;
import me.mrredness.infection.tasks.GameBarCountdownTask;
import me.mrredness.infection.tasks.LobbyBarCountdownTask;
import me.mrredness.infection.utils.BorderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Infection extends JavaPlugin {
    public Logger logger = this.getLogger();
    LogRecord enable = new LogRecord(Level.INFO, "Infection successfully loaded! Have fun!");
    LogRecord disable = new LogRecord(Level.INFO, "Infection disabled.");

    @Override
    public void onEnable() {
        logger.log(enable);
        getConfig().options().header("The following are the inventories given to players in the infection minigame. Please use caution if editing.");
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        InfectionSetupData.setup();
        InfectionSetupData.get().options().header("This file will be populated when /infection setup is run. Please do not manually edit this file unless you want to break things or it is absolutely necessary.");
        InfectionSetupData.get().options().copyDefaults(true);
        InfectionSetupData.save();
        Objects.requireNonNull(getCommand("infection")).setExecutor(new InfectionCommand(this));
        Objects.requireNonNull(getCommand("infection")).setTabCompleter(new InfectionTabCompletion());
        if (getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
            BorderUtils.removeBorder("Game");
            BorderUtils.removeBorder("Lobby");
        }
        getServer().getPluginManager().registerEvents(new ContainerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    @Override
    public void onDisable() {
        if (getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
            BorderUtils.removeBorder("Game");
            BorderUtils.removeBorder("Lobby");
        }
        LobbyBarCountdownTask.removeBar();
        GameBarCountdownTask.removeBar();
        try {
            if (!InfectionGame.getPlayersInGame().isEmpty()) {
                for (Player p : InfectionGame.getPlayersInGame()) {
                    InfectionGame.leaveGame(p);
                }
            }
        }
        catch (ConcurrentModificationException e) {
            System.out.println(e.getMessage());
        }
       if (!InfectionGame.isLobbyStage()) {
            InfectionGame.endGame(ChatColor.RED + "Server is reloading.", false);
        }
        Bukkit.getServer().getScheduler().cancelTasks(this);
        logger.log(disable);
    }

}

