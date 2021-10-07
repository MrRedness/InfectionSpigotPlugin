package me.mrredness.infection.utils;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import me.mrredness.infection.helpers.DataHelper;
import org.bukkit.Bukkit;

import java.util.HashMap;

//import org.bukkit.World;

public class BorderUtils {
    public static void setBorder(String borderType) {
        boolean borderWanted;
        boolean setupComplete;
        String borderHashMapName;
        String borderWorldSavedName;
        if (borderType.equalsIgnoreCase("lobby")) {
            borderWanted = DataHelper.checkBoolean("Infection Lobby Border");
            setupComplete = DataHelper.checkBoolean("Infection Lobby Setup Complete");
            borderHashMapName = "Infection Lobby Border Range";
            borderWorldSavedName = "Infection Lobby World";
        }
        else {
            borderWanted = DataHelper.checkBoolean("Infection Physical Border");
            setupComplete = DataHelper.checkBoolean("Infection Border Setup Complete");
            borderHashMapName = "Infection Border Range";
            borderWorldSavedName = "Infection Spawn World";
        }
        if (borderWanted && setupComplete && Bukkit.getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
            HashMap<String, Integer> range = DataHelper.getHashMap(borderHashMapName);
            if (range == null) {
                System.out.println("Something went wrong... Try restarting server.");
            } else if (Bukkit.getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
                double x1 = (double) range.get("xMin");
                double z1 = (double) range.get("zMin");
                double x2 = (double) range.get("xMax");
                double z2 = (double) range.get("zMax");

                String worldName = (String) DataHelper.get(borderWorldSavedName);

                Config.save(false);
                Config.setBorderCorners(worldName, x1, z1, x2, z2, false, false);
                if (borderHashMapName.equals("Infection Border Range")) {
                    Config.updateMessage("&cPlease stay within the boundaries of the infection arena.");
                } else {
                    Config.updateMessage("&cPlease stay within the boundaries of the infection lobby.");
                }
            }
        }
    }

    public static BorderData getBorder() {
        String worldName = (String) DataHelper.get("Infection Spawn World");
        return Config.Border(worldName);
    }

    public static void removeBorder(String borderType) {
        boolean borderWanted;
        boolean setupComplete;
        String borderWorldSavedName;
        if (borderType.equalsIgnoreCase("lobby")) {
            borderWanted = DataHelper.checkBoolean("Infection Lobby Border");
            setupComplete = DataHelper.checkBoolean("Infection Lobby Setup Complete");
            borderWorldSavedName = "Infection Lobby World";
        } else {
            borderWanted = DataHelper.checkBoolean("Infection Physical Border");
            setupComplete = DataHelper.checkBoolean("Infection Border Setup Complete");
            borderWorldSavedName = "Infection Spawn World";
        }
        if (borderWanted && setupComplete && Bukkit.getServer().getPluginManager().isPluginEnabled("WorldBorder")) {
            String worldName = (String) DataHelper.get(borderWorldSavedName);
            Config.removeBorder(worldName);
            Config.updateMessage("&cYou have reached the edge of this world.");
        }
    }

}
