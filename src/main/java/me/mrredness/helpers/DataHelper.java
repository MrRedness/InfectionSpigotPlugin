package me.mrredness.helpers;

import me.mrredness.infection.InfectionSetupData;
import org.bukkit.configuration.MemorySection;
//import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
//import java.util.Map;
import java.util.Map;
import java.util.Objects;

public class DataHelper {
    public static void addAndSave(String key, Object value) {
        InfectionSetupData.get().addDefault(key, "");
        InfectionSetupData.get().set(key,value);
        InfectionSetupData.save();
    }
    public static void addIfDoesNotExist(String key, Object value) {
        if (!contains(key)) {
            addAndSave(key, value);
        }
    }
    public static boolean check(String key, Object value) {
            return Objects.equals(InfectionSetupData.get().getString(key), value);
    }

    public static boolean checkBoolean(String key) {
        return InfectionSetupData.get().getBoolean(key);
    }

    public static boolean contains(String key) {
        return InfectionSetupData.get().contains(key);
    }

    public static Object get(String key) {
        return InfectionSetupData.get().get(key);
    }

    public static HashMap<String, Integer> getHashMap(String key) {
        try {
            MemorySection mem = (MemorySection) InfectionSetupData.get().get(key);
            Map<String, Integer> map = (Map) mem.getValues(false);
            //  Map<String, Integer> castedMap = (Map) map;
            HashMap<String, Integer> hashMap = (HashMap<String, Integer>) map;
            return hashMap;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

       /* try {
        return (HashMap) InfectionSetupData.get().get(key);
    //    }
    //    catch (Exception e) {
    //        return null;
     //   } */
    }
}
