package me.mrredness.infection.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class InfectionTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("join");
            options.add("leave");
            if (sender.hasPermission("infection.setup")) {
                options.add("setup");
            }
            if (sender.hasPermission("infection.forceStart")) {
                options.add("forcestart");
            }
            if (sender.hasPermission("infection.endGame")) {
                options.add("endgame");
            }
            return options;
        }
        if (args[0].equals("forcestart") && args.length == 2) {
            List<String> options = new ArrayList<>();
            options.add("1");
            options.add("15");
            options.add("30");
            options.add("45");
            options.add("60");
            return options;
        }
        return null;
    }
}
