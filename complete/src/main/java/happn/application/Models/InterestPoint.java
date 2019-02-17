package happn.application.Models;
import java.util.Objects;
public class InterestPoint {

    private String id;
    private double longitude;
    private double latitude;

    public InterestPoint(String id, double longitude, double latitude) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return this.id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void print() {
        System.out.println("interestPoint ("+ this.getId() + "," + this.getLatitude() + "," + this.getLongitude() + ")");
    }

    @Override
    public boolean equals(Object other) {
        if (this.id.equals(((InterestPoint)other).id))  {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.longitude, this.latitude);
    }
}
