package happn.application.Services;


import happn.application.Models.Grid;

import java.util.Map;

public interface GridService {
    Integer somme(Integer a, Integer b);
    void readDataFile();
    void printGridData();
    Map<String, String> getMostDenseZones(int n);
    Grid getGrid();
    Map<String, Integer> getZoneCountByParameters(double minLatitude, double minLongitude);
}
