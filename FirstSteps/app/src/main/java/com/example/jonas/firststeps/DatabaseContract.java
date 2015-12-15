package com.example.jonas.firststeps;

import android.provider.BaseColumns;

/**
 * Created by jonas on 14.12.15.
 */
public final class DatabaseContract {

    public static final  int    DATABASE_VERSION   = 3;
    public static final  String DATABASE_NAME      = "database.db";
    private static final String TEXT_TYPE          = " TEXT";
    private static final String COMMA_SEP          = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private DatabaseContract() {}

    public static abstract class Table1 implements BaseColumns {
        public static final String TABLE_NAME       = "nameOfTable";
        public static final String COLUMN_NAME_COL1 = "input";


        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_COL1 + TEXT_TYPE + " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}