package cs300.beelazi.MapModules;

import java.io.Serializable;


public class MapCoord implements Serializable{
    private double latitude;
    private double longitude;

    public MapCoord(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}


