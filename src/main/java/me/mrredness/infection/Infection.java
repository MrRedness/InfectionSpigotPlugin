package me.mrredness.infection;

import me.mrredness.infection.commands.InfectionCommand;
import me.mrredness.infection.commands.InfectionTabCompletion;
import me.mrredness.infection.listeners.*;
import me.mrredness.infection.tasks.LobbyBarCountdownTask;
import me.mrredness.infection.utils.BorderUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class Infection extends JavaPlugin {
    public Logger logger = this.getLogger();
    LogRecord enable = new LogRecord(Level.INFO, "Infection successfully loaded! Have fun!");
    LogRecord disable = new LogRecord(Level.INFO, "Infection disabled.");
    boolean worldBorderEnabled;

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
        Objects.requireNonNull(getCommand("infection")).setExecutor(new InfectionCommand(this, worldBorderEnabled));
        Objects.requireNonNull(getCommand("infection")).setTabCompleter(new InfectionTabCompletion());
        worldBorderEnabled = getServer().getPluginManager().isPluginEnabled("WorldBorder");
        if (worldBorderEnabled) {
            BorderUtils.removeBorder("Infection Spawn Setup Complete", "Infection Spawn World");
        }
        if (worldBorderEnabled) {
            BorderUtils.removeBorder("Infection Lobby Setup Complete", "Infection Lobby World");
        }
        getServer().getPluginManager().registerEvents(new ContainerListener(worldBorderEnabled), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
    }

    @Override
    public void onDisable() {
        if (worldBorderEnabled) {
            BorderUtils.removeBorder("Infection Spawn Setup Complete", "Infection Spawn World");
        }
        if (worldBorderEnabled) {
            BorderUtils.removeBorder("Infection Lobby Setup Complete", "Infection Lobby World");
        }
        LobbyBarCountdownTask.removeAll();
        Bukkit.getServer().getScheduler().cancelTasks(this);
        if (InfectionGame.isLobbyStage()) {
            InfectionGame.endGame(ChatColor.RED + "Server is reloading.");
        }
        for (Player p : InfectionGame.getPlayersInGame()) {
            InfectionGame.leaveGame(p);
        }
        logger.log(disable);
    }

}

