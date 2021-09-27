package me.mrredness.infection;

import me.mrredness.infection.commands.InfectionCommand;
import me.mrredness.infection.commands.InfectionTabCompletion;
import me.mrredness.infection.listeners.ChatListener;
import me.mrredness.infection.listeners.ContainerListener;
import me.mrredness.infection.listeners.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

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
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        InfectionSetupData.setup();
        InfectionSetupData.get().options().header("This file will be populated when /infection setup is run. Please do not manually edit this file unless you want to break things or it is absolutely necessary.");
        InfectionSetupData.get().options().copyDefaults(true);
        InfectionSetupData.save();
        Objects.requireNonNull(getCommand("infection")).setExecutor(new InfectionCommand(this));
        Objects.requireNonNull(getCommand("infection")).setTabCompleter(new InfectionTabCompletion());
        getServer().getPluginManager().registerEvents(new ContainerListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
    }
    @Override
    public void onDisable() {
        logger.log(disable);
    }

}

