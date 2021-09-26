package me.mrredness.infection;
import java.util.HashMap;

import com.wimbli.WorldBorder.BorderData;
import com.wimbli.WorldBorder.Config;

import org.bukkit.WorldBorder;

import me.mrredness.infection.commands.DataHelper;

import org.bukkit.World;

public class BorderUtils {
       public static void setBorder() {
           HashMap<String, Integer> range = DataHelper.getHashMap("Infection Border Range");
           double x1 = (double) range.get("xMin");
           double z1 = (double) range.get("zMin");
           double x2 = (double) range.get("xMax");
           double z2 = (double) range.get("zMax");

           String worldName = (String) DataHelper.get("Infection Border World");

           Config.setBorderCorners(worldName, x1, z1, x2, z2);
       }
       public static BorderData getBorder() {
           String worldName = (String) DataHelper.get("Infection Border World");
           return Config.Border(worldName);
       }
       public static void removeBorder() {
           String worldName = (String) DataHelper.get("Infection Border World");
           Config.removeBorder(worldName);
       }
    
}
