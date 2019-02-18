package happn.application.Models;

import java.util.*;

public class Grid {

    HashSet<Zone> zones;
    // Grid increment (default value is 0.5)
    private static double increment;

    public Grid(double increment) {
        this.increment = increment;
        this.zones = new HashSet<>();
    }

    public void addZone(Zone zone) {
        this.zones.add(zone);
    }

    public static double getIncrement() {
        return increment;
    }

    public static void setIncrement(double increment) {
        Grid.increment = increment;
    }

    public HashSet<Zone> getZones() {
        return this.zones;
    }

    public Map getResult() {
        Map<String, String> result = new HashMap<>();
        result.put("result","test result");
        return result;
    }
}
