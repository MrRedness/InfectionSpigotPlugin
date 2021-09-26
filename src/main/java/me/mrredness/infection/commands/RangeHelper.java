package me.mrredness.infection.commands;

import org.bukkit.Location;

import java.util.HashMap;


public class RangeHelper {
    public static boolean isInRange(int bound1, int bound2, int numberToTest) {
        int min = Math.min(bound1, bound2);
        int max = Math.max(bound1, bound2);
        return (numberToTest >= min && numberToTest <= max);
    }
    public static boolean isSpawnInLocationRange(Location pos1, Location pos2, Location spawn) {
        int pos1x = (int) pos1.getX();
        int pos1y = (int) pos1.getY();
        int pos1z = (int) pos1.getZ();

        int pos2x = (int) pos2.getX();
        int pos2y = (int) pos2.getY();
        int pos2z = (int) pos2.getZ();

        int spawnX = (int) spawn.getX();
        int spawnY = (int) spawn.getY();
        int spawnZ = (int) spawn.getZ();

        boolean checkX = isInRange(pos1x, pos2x, spawnX);
        boolean checkY = isInRange(pos1y, pos2y, spawnY);
        boolean checkZ = isInRange(pos1z, pos2z, spawnZ);

        return (checkX && checkY && checkZ);
    }
    public static HashMap<String, Integer> createCoordinateRange(Location pos1, Location pos2) {
        int pos1x = (int) pos1.getX();
        int pos1y = (int) pos1.getY();
        int pos1z = (int) pos1.getZ();

        int pos2x = (int) pos2.getX();
        int pos2y = (int) pos2.getY();
        int pos2z = (int) pos2.getZ();

        int xMin = Math.min(pos1x, pos2x);
        int yMin = Math.min(pos1y, pos2y);
        int zMin = Math.min(pos1z, pos2z);

        int xMax = Math.max(pos1x, pos2x);
        int yMax = Math.max(pos1y, pos2y);
        int zMax = Math.max(pos1z, pos2z);

        int xAmp = (xMax - xMin) / 2;
        int yAmp = (yMax - yMin) / 2;
        int zAmp = (zMax - zMin) / 2;

        int xCenter = (xMax - xAmp);
        int yCenter = (yMax - yAmp);
        int zCenter = (zMax - zAmp);



        HashMap<String, Integer> range = new HashMap<>();

        range.put("xMin", xMin);
        range.put("xMax", xMax);

        range.put("yMin", yMin);
        range.put("yMax", yMax);

        range.put("zMin", zMin);
        range.put("zMax", zMax);

        range.put("xAmp", xAmp);
        range.put("yAmp", yAmp);
        range.put("zAmp", zAmp);

        range.put("xCenter", xCenter);
        range.put("yCenter", yCenter);
        range.put("zCenter", zCenter);

        return range;


    }
}
