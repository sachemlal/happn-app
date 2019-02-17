package happn.application.Services;


import happn.application.Models.Grid;

public interface GridService {
    Integer somme(Integer a, Integer b);
    void readDataFile();
    void printGridData();
    void getMostDenseZones(int n);
    Grid getGrid();
}
