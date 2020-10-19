package com.anon.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Trip {

    private String name;
    private Date start;
    private Date end;
    private float distance;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    public Trip(String name, Date start, Date end, float distance){
        this.name = name;
        this.start = start;
        this.end = end;
        this.distance = distance;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",").append(sdf.format(start)).append(",").
            append(sdf.format(end)).append(",").append(distance);
        return sb.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
