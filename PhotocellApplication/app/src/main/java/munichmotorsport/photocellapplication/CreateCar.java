package munichmotorsport.photocellapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import db.CarDao;
import db.DaoMaster;
import db.DaoSession;

public class CreateCar extends AppCompatActivity {

    CarDao carDao;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        setTitle("Neues Auto erstellen");

        spinner = (Spinner) findViewById(R.id.teams);

        ArrayList<String> teams = new ArrayList<>();
        // TODO: load teams
        // TODO: add teams to  teams
        teams.add("municHMotorsport");  // Bsp:
        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dropDown.addAll(teams);
        spinner.setAdapter(dropDown);
    }

    /**
     * Auto erstellen
     * @param view
     */
    public void createCar(View view){
        // TODO: Auto in DB schreiben
    }

    /**
     * zur Activity "Team erstellen"
     * @param view
     */
    public void createTeam(View view){
        Intent intent = new Intent(this, CreateTeam.class);
        startActivity(intent);
    }

}
