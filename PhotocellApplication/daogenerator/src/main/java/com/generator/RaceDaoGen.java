package com.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;
import de.greenrobot.daogenerator.ToOne;

public class RaceDaoGen {
static Entity car;
static Entity race;
static Entity lap;
static Entity config;
static Entity team;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "db");
        createTeam(schema);
        createCar(schema);
        createConfig(schema);
        createRace(schema);
        createLap(schema);
        new DaoGenerator().generateAll(schema, "app/src-gen/main/java");
    }

    private static void createRace(Schema schema) {
        race = schema.addEntity("Race");
        race.addIdProperty().autoincrement().primaryKey();
        race.addStringProperty("type").notNull();
        race.addStringProperty("description");
    }

    private static void createLap(Schema schema){
        lap = schema.addEntity("Lap");
        lap.addIdProperty().autoincrement().primaryKey();
        lap.addLongProperty("Time").notNull();
        lap.addIntProperty("Number").notNull();
        Property raceID = lap.addLongProperty("raceID").notNull().getProperty();
        Property configID = lap.addLongProperty("configID").notNull().getProperty();
        ToMany configToLaps = config.addToMany(lap, configID);
        ToMany raceToLaps = race.addToMany(lap, raceID);
    }

    private static void createConfig(Schema schema) {
        config = schema.addEntity("Config");
        config.addIdProperty().autoincrement().primaryKey();
        config.addIntProperty("Barcode");
        config.addStringProperty("Comment");
        Property carID = config.addLongProperty("carID").notNull().getProperty();
        ToOne carToConfig = config.addToOne(car, carID);
    }

    private static void createCar(Schema schema) {
        car = schema.addEntity("Car");
        car.addIdProperty().autoincrement().primaryKey();
        car.addStringProperty("name").notNull();
        Property teamID = car.addLongProperty("teamID").notNull().getProperty();
        ToMany teamToCars = team.addToMany(car, teamID);
    }

    private static void createTeam(Schema schema) {
        team = schema.addEntity("Team");
        team.addIdProperty().autoincrement().primaryKey();
        team.addStringProperty("Name").notNull();
    }

}
