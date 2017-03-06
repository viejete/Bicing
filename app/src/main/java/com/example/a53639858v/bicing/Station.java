package com.example.a53639858v.bicing;

public class Station {

    private String id;
    private String type;
    private String latitude;
    private String longitude;
    private String streetName;
    private String streetNumber;
    private String photoUrl;
    private int totalBikes;
    private int bikeAvailables;

    public Station() {}

    public Station(String id, String type, String latitude,
                   String longitude, String streetName, String streetNumber,
                   int totalBikes, int bikeAvailables) {
        this.id = id;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.totalBikes = totalBikes;
        this.bikeAvailables = bikeAvailables;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public int getTotalBikes() {
        return totalBikes;
    }

    public void setTotalBikes(int totalBikes) {
        this.totalBikes = totalBikes;
    }

    public int getBikeAvailables() {
        return bikeAvailables;
    }

    public void setBikeAvailables(int bikeAvailables) {
        this.bikeAvailables = bikeAvailables;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
