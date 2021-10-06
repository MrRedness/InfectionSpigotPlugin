package me.mrredness.infection.utils;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;
import me.mrredness.infection.helpers.DataHelper;

import java.util.HashMap;

//import org.bukkit.World;

public class BorderUtils {
    public static void setBorder(String borderHashMapName, String borderWorldSavedName) {
        HashMap<String, Integer> range = DataHelper.getHashMap(borderHashMapName);
        if (range == null) {
            System.out.println("Something went wrong...");
        } else {
            double x1 = (double) range.get("xMin");
            double z1 = (double) range.get("zMin");
            double x2 = (double) range.get("xMax");
            double z2 = (double) range.get("zMax");

            String worldName = (String) DataHelper.get(borderWorldSavedName);

            Config.save(false);
            Config.setBorderCorners(worldName, x1, z1, x2, z2, false, false);
            if (borderHashMapName.equals("Infection Border Range")) {
                Config.updateMessage("&cPlease stay within the boundaries of the infection arena.");
            } else if (borderHashMapName.equals("Infection Lobby Border Range")) {
                Config.updateMessage("&cPlease stay within the boundaries of the infection lobby.");
            }
        }
    }

    public static BorderData getBorder() {
        String worldName = (String) DataHelper.get("Infection Spawn World");
        return Config.Border(worldName);
    }

    public static void removeBorder(String setupCompleteBooleanName, String borderWorldSavedName) {
        if (DataHelper.checkBoolean(setupCompleteBooleanName)) {
            String worldName = (String) DataHelper.get(borderWorldSavedName);
            Config.removeBorder(worldName);
            Config.updateMessage("&cYou have reached the edge of this world.");
        }
    }

}
