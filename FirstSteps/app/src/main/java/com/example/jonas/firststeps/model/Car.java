package com.example.jonas.firststeps.model;

/**
 * Created by nilsgr
 */
public class Car {

    private int id;
    private String name;
    private int ps;

    public Car() {
    }

    public Car(int id, String name, int ps) {
        this.id = id;
        this.name = name;
        this.ps = ps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }
}
