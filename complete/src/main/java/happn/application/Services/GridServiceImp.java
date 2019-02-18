package happn.application.Services;

import happn.application.Models.Grid;
import happn.application.Models.InterestPoint;
import happn.application.Models.Zone;
import org.springframework.stereotype.Service;
import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GridServiceImp implements GridService {

    private Grid grid;

    public Integer somme(Integer a, Integer b) {
        return a+b;
    }

    public void readDataFile() {
        try{
            this.readWithCsvMapReader();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void readWithCsvMapReader() throws Exception {

        ICsvMapReader mapReader = null;
        try {
            mapReader = new CsvMapReader(new FileReader("../data.tsv"), CsvPreference.TAB_PREFERENCE);

            // the header columns are used as the keys to the Map
            final String[] header = mapReader.getHeader(true);
            final CellProcessor[] processors = getProcessors();
            Map<String, Object> interestPointMap;

            this.grid = new Grid(0.5);
            while( (interestPointMap = mapReader.read(header, processors)) != null ) {
                InterestPoint interestPoint = new InterestPoint((String)interestPointMap.get("@id"), (double)interestPointMap.get("@lon"), (double)interestPointMap.get("@lat"));
                ArrayList newZones = generateZonesFromInterestPoint(interestPoint);

                for (Iterator<Zone> i = newZones.iterator(); i.hasNext();) {
                    Zone newZone = i.next();
                    if(this.grid.getZones().contains(newZone)) {
                        for (Iterator<Zone> i2 = this.grid.getZones().iterator(); i2.hasNext();) {
                            Zone existingZone = i2.next();
                            if(existingZone.equals(newZone)) {
                                for (Iterator<InterestPoint> i3 = newZone.getInterestPoints().iterator(); i3.hasNext();) {
                                    InterestPoint tmpInterestPoint = i3.next();
                                    existingZone.getInterestPoints().add(tmpInterestPoint);
                                }
                            }
                        }
                    } else {
                        this.grid.addZone(newZone);
                    }
                }
            }

        }
        finally {
            if( mapReader != null ) {
                mapReader.close();
            }
        }
    }

    // this function helps getting the closest interval value depending on whether we want the minor limit of the interval or the superior one
    private double getValueByType(double value, String type) {

        double length = value - Math.floor(value);

        if(type.equals("inf")) {
            return (length > Grid.getIncrement()) ? Math.floor(value) + Grid.getIncrement() : Math.floor(value);
        } else {
            return (length > Grid.getIncrement()) ? Math.ceil(value) : Math.floor(value) + Grid.getIncrement();
        }
    }

    // if an interest point is in the edge between two zones it will appear in all of the concerned zones example : (0.0) appear in four zones
    private ArrayList<Zone> generateZonesFromInterestPoint(InterestPoint interestPoint) {
        ArrayList<Zone> zones = new ArrayList<>();

        if(interestPoint.getLongitude() - Math.floor(interestPoint.getLongitude()) == Grid.getIncrement() || interestPoint.getLongitude() - Math.floor(interestPoint.getLongitude()) == 0) {
            Zone zoneLongitudeInf = new Zone(
                    interestPoint.getLongitude() - Grid.getIncrement(),
                    getValueByType(interestPoint.getLatitude(), "inf"),
                    interestPoint.getLongitude(),
                    getValueByType(interestPoint.getLatitude(), "sup"));
            zoneLongitudeInf.getInterestPoints().add(interestPoint);
            zones.add(zoneLongitudeInf);

            Zone zoneLongitudeSup = new Zone(
                    interestPoint.getLongitude(),
                    getValueByType(interestPoint.getLatitude(), "inf"),
                    interestPoint.getLongitude() + Grid.getIncrement(),
                    getValueByType(interestPoint.getLatitude(), "sup"));
            zoneLongitudeSup.getInterestPoints().add(interestPoint);
            zones.add(zoneLongitudeSup);
        }
        else if(Math.abs(interestPoint.getLatitude() - Math.floor(interestPoint.getLatitude())) == Grid.getIncrement() || Math.abs(interestPoint.getLatitude() - Math.floor(interestPoint.getLatitude())) == 0) {
            Zone zoneLatitudeInf = new Zone(
                    getValueByType(interestPoint.getLongitude(), "inf"),
                    interestPoint.getLatitude() - Grid.getIncrement(),
                    getValueByType(interestPoint.getLongitude(), "sup"),
                    interestPoint.getLatitude());
            zoneLatitudeInf.getInterestPoints().add(interestPoint);
            zones.add(zoneLatitudeInf);

            Zone zoneLatitudeSup = new Zone(
                    getValueByType(interestPoint.getLongitude(), "inf"),
                    interestPoint.getLatitude(),
                    getValueByType(interestPoint.getLongitude(), "sup"),
                    interestPoint.getLatitude() + Grid.getIncrement());
            zoneLatitudeSup.getInterestPoints().add(interestPoint);
            zones.add(zoneLatitudeSup);
        } else {
            Zone zone = new Zone(
                    getValueByType(interestPoint.getLongitude(), "inf"),
                    getValueByType(interestPoint.getLatitude(), "inf"),
                    getValueByType(interestPoint.getLongitude(), "sup"),
                    getValueByType(interestPoint.getLatitude(), "sup"));
            zone.getInterestPoints().add(interestPoint);
            zones.add(zone);
        }

        return zones;
    }

    public void printGridData() {
       System.out.println("# printGridData #");
       for (Iterator<Zone> i = this.grid.getZones().iterator(); i.hasNext();) {
            Zone zone = i.next();
            zone.print();
            System.out.println("size ("+zone.getInterestPoints().size()+")");
        }
    }

    // Get number of interestPoints based on params
    public Map<String, Integer> getZoneCountByParameters(double minLatitude, double minLongitude) {
        List<Zone> criteriaZone = this.grid.getZones().stream()
                .filter(zone -> zone.getMinimumLatitude() == minLatitude && zone.getMinimumLongitude() == minLongitude).collect(Collectors.toList());

        int count = 0;
        for (Iterator<Zone> i = criteriaZone.iterator(); i.hasNext();) {
            Zone zone = i.next();
            count += zone.getInterestPoints().size();
        }

        Map<String, Integer> result = new HashMap<>();
        result.put("result", count);
        return result;
    }

    // Get the first nth most dense zone
    public Map<String, String> getMostDenseZones(int zoneCount) {
        List<Zone> zones = new ArrayList<>(this.grid.getZones());
        Collections.sort(zones, new Comparator<Zone>() {
            @Override
            public int compare(Zone zone1, Zone zone2) {
                return Integer.valueOf(zone2.getInterestPoints().size()).compareTo(zone1.getInterestPoints().size());
            }
        });

        List<Zone> mostDenseZones = zones.stream().limit(zoneCount).collect(Collectors.toList());
        Map<String, String> result = new HashMap<>();
        int iter=0;
        for (Iterator<Zone> i = mostDenseZones.iterator(); i.hasNext();) {
            Zone zone = i.next();
            result.put("zone_"+iter, "Zone ("+zone.getMinimumLatitude() + "," +zone.getMaximumLatitude() + "," + zone.getMinimumLongitude() + "," + zone.getMaximumLongitude() + ")");
            iter++;
        }
        return result;
    }

    private static CellProcessor[] getProcessors() {

        final CellProcessor[] processors = new CellProcessor[] {
                new UniqueHashCode(), // interestPointNo (must be unique)
                new DMinMax(DMinMax.MIN_SHORT, DMinMax.MAX_DOUBLE), // longitude
                new DMinMax(DMinMax.MIN_SHORT, DMinMax.MAX_DOUBLE) // latitude
        };

        return processors;
    }

    public Grid getGrid() {
        return grid;
    }
}
