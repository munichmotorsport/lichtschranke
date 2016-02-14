package com.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class RaceDaoGen {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "db");
        createRace(schema);
        new DaoGenerator().generateAll(schema, "./app/src-gen/main/java");
    }

    private static void createRace(Schema schema) {
        Entity race = schema.addEntity("Race");
        race.addIdProperty();
        race.addStringProperty("type").notNull();
        race.addStringProperty("name");
    }
}
