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

    public static Location generateLocation(){

        //Generate Random Location
        Random random = new Random();

        HashMap<String, Integer> range = DataHelper.getHashMap("Infection Border Range");
        int xCenter = range.get("xCenter");
//debug        p.sendMessage("xCenter is " + xCenter);
        int zCenter = range.get("zCenter");
//debug       p.sendMessage("zCenter is " + zCenter);

        int xAmp = range.get("xAmp");
//debug        p.sendMessage("xAmp is " + xAmp);
        int zAmp = range.get("zAmp");
//debug       p.sendMessage("zAmp is " + zAmp);

        int x = random.nextInt(xAmp);
//debug        p.sendMessage("x is " + x);
        int z = random.nextInt(zAmp);
//debug         p.sendMessage("z is " + z);
        int y = 150;

        if (random.nextBoolean()) {
            x = xCenter + x;
//debug             p.sendMessage("New x is " + x);
            if (random.nextBoolean()) {
                z = zCenter + z;
//debug                 p.sendMessage("New z is " + z);
            } else {
                z = zCenter - z;
//debug                 p.sendMessage("New z is " + z);
            }
        } else {
            x = xCenter - x;
//debug             p.sendMessage("New x is " + x);
            if (random.nextBoolean()) {
                z = zCenter + z;
//debug                 p.sendMessage("New z is " + z);
            } else {
                z = zCenter - z;
//debug                 p.sendMessage("New z is " + z);
            }
        }


        Location randomLocation = new Location(((Location) DataHelper.get("Infection Spawn Location")).getWorld(), x, y, z);
        y = Objects.requireNonNull(randomLocation.getWorld()).getHighestBlockYAt(randomLocation) + 1;
        randomLocation.setY(y);
        return randomLocation;
    }
    public static Location findSafeLocation(){
        Location randomLocation = generateLocation();
        while (!isLocationSafe(randomLocation)){
            //Keep looking for a safe location
            randomLocation = generateLocation();
        }
        return randomLocation;
    }
    public static boolean isLocationSafe(Location location){
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        //Get instances of the blocks around where the player would spawn
        Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y, z);
        Block below = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y - 1, z);
        Block above = Objects.requireNonNull(location.getWorld()).getBlockAt(x, y + 1, z);
        //Check to see if the surroundings are safe or not
        return !(bad_blocks.contains(below.getType())) || (block.getType().isSolid()) || (above.getType().isSolid());
    }
}