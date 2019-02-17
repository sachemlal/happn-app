package happn.application.Models;

import java.util.*;

public class Zone {

    private HashSet<InterestPoint> interestPoints;

    private double minimumLongitude;
    private double minimumLatitude;
    private double maximumLongitude;
    private double maximumLatitude;

    public Zone(double minimumLongitude,double minimumLatitude,double maximumLongitude,double maximumLatitude) {
        this.interestPoints = new HashSet<>();
        this.minimumLongitude = minimumLongitude;
        this.minimumLatitude = minimumLatitude;
        this.maximumLongitude = maximumLongitude;
        this.maximumLatitude = maximumLatitude;
    }

    public HashSet<InterestPoint> getInterestPoints() {
        return interestPoints;
    }

    public double getMinimumLongitude() {
        return minimumLongitude;
    }

    public void setMinimumLongitude(double minimumLongitude) {
        this.minimumLongitude = minimumLongitude;
    }

    public double getMinimumLatitude() {
        return minimumLatitude;
    }

    public void setMinimumLatitude(double minimumLatitude) {
        this.minimumLatitude = minimumLatitude;
    }

    public double getMaximumLongitude() {
        return maximumLongitude;
    }

    public void setMaximumLongitude(double maximumLongitude) {
        this.maximumLongitude = maximumLongitude;
    }

    public double getMaximumLatitude() {
        return maximumLatitude;
    }

    public void setMaximumLatitude(double maximumLatitude) {
        this.maximumLatitude = maximumLatitude;
    }

    public void print() {
        System.out.println("Zone ("+this.getMinimumLatitude() + "," +this.getMaximumLatitude() + "," + this.getMinimumLongitude() + "," + this.getMaximumLongitude() + ")");
        for (Iterator<InterestPoint> i = this.interestPoints.iterator(); i.hasNext();)
        {
            InterestPoint interestPoint = i.next();
            interestPoint.print();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this.maximumLongitude == ((Zone)other).maximumLongitude &&
                this.minimumLongitude == ((Zone)other).minimumLongitude &&
                this.maximumLatitude == ((Zone)other).maximumLatitude &&
                this.minimumLatitude == ((Zone)other).minimumLatitude)  {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.minimumLongitude, this.minimumLatitude, this.maximumLongitude, this.maximumLatitude);
    }
}
