package com.example.isincountry;

public class LocationReq {
    public double lon;
    public double lat;
    public String countryCode;

    public LocationReq(double lon, double lat, String countryCode) {
        this.lon = lon;
        this.lat = lat;
        this.countryCode = countryCode;
    }
}
