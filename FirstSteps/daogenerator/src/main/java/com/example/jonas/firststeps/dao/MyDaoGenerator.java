package com.example.jonas.firststeps.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1000, "com.example.jonas.firststeps.db");
        addCar(schema);
        new DaoGenerator().generateAll(schema, "../app/src-gen/main/java");
    }

    private static void addCar(Schema schema) {
        Entity car = schema.addEntity("Car");
        car.addIdProperty();
        car.addStringProperty("name").notNull();
        car.addStringProperty("ps");
    }

}
