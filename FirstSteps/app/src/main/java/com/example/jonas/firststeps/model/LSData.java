package com.example.jonas.firststeps.model;

public class LSData {
    private String timeStamp;
    private String id;

    public LSData(String timeStamp, String id) {
        this.timeStamp = timeStamp;
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "timeStamp='" + timeStamp + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
