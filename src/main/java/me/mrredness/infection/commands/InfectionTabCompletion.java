package me.mrredness.infection.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class InfectionTabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1){
            List<String> options = new ArrayList<>();
            options.add("join");
            options.add("leave");
            options.add("setup");
            return options;
        }
        return null;
    }
}
