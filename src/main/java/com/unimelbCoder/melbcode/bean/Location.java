package com.unimelbCoder.melbcode.bean;

public class Location {
    private int id;

    private String location_name;

    private String description;

    private String openning_datetime;

    private String type;

    private double latitude;

    private double longitude;

    private String city_name;

    private String district_name;

    private int favorite_count;

    private int visited_count;

    public Location(int id, String location_name, String description, String openning_datetime, String type, double latitude, double longitude, String city_name, String district_name, int favorite_count, int visited_count) {
        this.id = id;
        this.location_name = location_name;
        this.description = description;
        this.openning_datetime = openning_datetime;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city_name = city_name;
        this.district_name = district_name;
        this.favorite_count = favorite_count;
        this.visited_count = visited_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenning_datetime() {
        return openning_datetime;
    }

    public void setOpenning_datetime(String openning_datetime) {
        this.openning_datetime = openning_datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getVisited_count() {
        return visited_count;
    }

    public void setVisited_count(int visited_count) {
        this.visited_count = visited_count;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", location_name='" + location_name + '\'' +
                ", description='" + description + '\'' +
                ", openning_datetime='" + openning_datetime + '\'' +
                ", type='" + type + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude=" + longitude + '\'' +
                ", city_name=" + city_name + '\'' +
                ", district_name=" + district_name + '\'' +
                ", favorite_count=" + favorite_count + '\'' +
                ", visited_count=" + visited_count + '\'' +
            '}';
    }
}
