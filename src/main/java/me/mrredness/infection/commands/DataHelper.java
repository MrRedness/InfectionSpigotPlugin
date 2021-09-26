package me.mrredness.infection.commands;

import me.mrredness.infection.InfectionSetupData;
import org.bukkit.configuration.MemorySection;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataHelper {
    public static void addAndSave(String key, Object value) {
        InfectionSetupData.get().addDefault(key, "");
        InfectionSetupData.get().set(key,value);
        InfectionSetupData.save();
    }
    public static boolean check(String key, Object value) {
            return Objects.equals(InfectionSetupData.get().getString(key), value);
    }
    public static Object get(String key) {
        return InfectionSetupData.get().get(key);
    }
    public static HashMap<String, Integer> getHashMap(String key) {
        MemorySection mem = (MemorySection) InfectionSetupData.get().get(key);
      //  Map<String, Integer> map = (Map) Objects.requireNonNull(mem).getValues(false);
      //  Map<String, Integer> castedMap = (Map) map;
        try {
        return new HashMap<String, Integer>((Map) Objects.requireNonNull(mem).getValues(false));
        }
        catch (Exception e) {
            return null;
            
        }
    }
}
