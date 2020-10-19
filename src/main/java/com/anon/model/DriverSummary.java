package com.anon.model;

public class DriverSummary {

    private String name;
    private int distance;
    private long averageSpeed;

    public DriverSummary(String name, int distance,int averageSpeed) {
        this.name = name;
        this.distance = distance;
        this.averageSpeed = averageSpeed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(", ").append(distance).append(",").append(averageSpeed);
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public long getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(long averageSpeed) {
        this.averageSpeed = averageSpeed;
    }
}
