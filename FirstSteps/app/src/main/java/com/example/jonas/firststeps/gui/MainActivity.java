package com.example.jonas.firststeps.gui;


import android.content.Context;
import android.content.Intent;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jonas.firststeps.db.Car;
import com.example.jonas.firststeps.db.CarDao;
import com.example.jonas.firststeps.db.DaoMaster;
import com.example.jonas.firststeps.db.DaoSession;
import com.example.jonas.firststeps.dbAccess.DatabaseContract;
import com.example.jonas.firststeps.dbAccess.DatabaseHelper;
import com.example.jonas.firststeps.R;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.jonas.firststeps.db.DaoMaster.*;


public class MainActivity extends AppCompatActivity {

    @Bind(R.id.btn_clearList)
    Button btn_clearList;

    ArrayList<String> stringArray = new ArrayList<>();
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Erste Gehversuche mit Datenbanken2");
        ButterKnife.bind(this);

        final Button btn_next = (Button) findViewById(R.id.btn_next);

        btn_clearList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearList();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goTo_nextActivity();
            }
        });

        showData();

        testDao();
    }

    private void testDao() {
        DevOpenHelper helper = new DevOpenHelper(this, "lichtschranke_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        CarDao carDao = daoSession.getCarDao();
        Car car = new Car(null, "TestCar!", "100");
        long rowID = carDao.insert(car);
        Timber.e("Inserted with rowID: %s", rowID);


        carDao.deleteByKey(1L);

        Timber.i("Car with id 1: %s", carDao.load(1L));

        for (Car loadedCar : carDao.loadAll()) {
            Timber.i("Cars from Db: %s id: %s", loadedCar.getName(), loadedCar.getId());
        }
    }


    public void saveData(View view) {
        addData();
        stringArray.clear();
        showData();
    }

    public void deleteData(View view) {
        EditText editText = (EditText) findViewById(R.id.deleteText);
        String message = editText.getText().toString();
        deleteDataDB(message);
        stringArray.clear();
        showData();
    }

    public void showData() {
        stringArray.addAll(getData(this));
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }


    public static ArrayList<String> getData(Context context) {


        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Get the database. If it does not exist, this is where it will
        // also be created.
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String selectQuery = "SELECT  * FROM " + DatabaseContract.Table1.TABLE_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<String> data = new ArrayList<>();


        if (cursor.moveToFirst()) {
            do {
                data.add(cursor.getString(1));
            } while (cursor.moveToNext());
            db.close();
        }
        return data;

    }

    public void addData() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the database. If it does not exist, this is where it will
        // also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create insert entries
        ContentValues values = new ContentValues();
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        values.put(DatabaseContract.Table1.COLUMN_NAME_COL1, message);


        long newRowId = db.insert(
                DatabaseContract.Table1.TABLE_NAME,
                null,
                values);

        db.close();
    }


    public void clearList() {
        stringArray.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void goTo_nextActivity() {
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("MainActivity.Data", stringArray);
        startActivity(intent);
    }


    public void deleteDataDB(String message) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Get the database. If it does not exist, this is where it will
        // also be created.
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        db.delete(DatabaseContract.Table1.TABLE_NAME, DatabaseContract.Table1.COLUMN_NAME_COL1 + " = '" + message + "'", null);

        db.close();
    }

}


