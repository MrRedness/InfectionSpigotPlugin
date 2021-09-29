package me.mrredness.infection;

import me.mrredness.infection.commands.DataHelper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;

public class TeleportUtils {

    public static HashSet<Material> bad_blocks = new HashSet<>();

    static{
        bad_blocks.add(Material.LAVA);
        bad_blocks.add(Material.FIRE);
        bad_blocks.add(Material.CACTUS);
    }

    public static Location generateLocation(HashMap<String,Integer> range){

        //Generate Random Location
        Random random = new Random();

        int xCenter = range.get("xCenter");
        int zCenter = range.get("zCenter");

        int xAmp = range.get("xAmp");
        int zAmp = range.get("zAmp");

        int x = random.nextInt(xAmp);
        int z = random.nextInt(zAmp);
        int y = 150;

        if (random.nextBoolean()) {
            x = xCenter + x;
        } else {
            x = xCenter - x;
        }
        if (random.nextBoolean()) {
            z = zCenter + z;
        } else {
            z = zCenter - z;
        }


        Location randomLocation = new Location(((Location) DataHelper.get("Infection Spawn Location")).getWorld(), x, y, z);
        y = Objects.requireNonNull(randomLocation.getWorld()).getHighestBlockYAt(randomLocation) + 1;
        randomLocation.setY(y);
        return randomLocation;
    }
    public static Location findSafeLocation(HashMap<String, Integer> range){
        Location spawn = (Location) DataHelper.get("Infection Spawn Location");
        Location randomLocation = generateLocation(Objects.requireNonNull(range));

        while (!isLocationSafe(randomLocation, spawn, range)){
            //Keep looking for a safe location
            randomLocation = generateLocation(range);
        }
        return randomLocation;
    }
    public static boolean isLocationSafe(Location location, Location spawn, HashMap<String, Integer> range){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        //Get instances of the blocks around where the player would spawn
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
        Block below = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y - 1, z);
        Block above = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y + 1, z);
        //Check to see if the surroundings are safe or not

        //Check to see if location is far enough from spawn
        int spawnX = spawn.getBlockX();
        int spawnZ = spawn.getBlockZ();

        double newXAmp = ((double) range.get("xAmp")) * .3;
        double newZAmp = ((double) range.get("zAmp")) * .3;

        boolean checkX = (Math.abs(x - spawnX) < newXAmp);
        boolean checkZ = (Math.abs(z - spawnZ) < newZAmp);

        boolean checkBelow = bad_blocks.contains(below.getType());
        boolean checkBlock = block.getType().isSolid();
        boolean checkAbove = (above.getType().isSolid());

       /* System.out.println("check X" + checkX);
        System.out.println("check Y" + checkZ);
        System.out.println("checkBelow" + checkBelow);
        System.out.println("checkBlock" + checkBlock);
        System.out.println("checkAbove" + checkAbove);
*/

        return !(checkBelow || checkBlock || checkAbove || checkX || checkZ);
    }
}