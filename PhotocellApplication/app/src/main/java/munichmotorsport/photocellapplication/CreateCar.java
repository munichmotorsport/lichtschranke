package munichmotorsport.photocellapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import db.Car;
import db.CarDao;
import db.DaoMaster;
import db.DaoSession;
import db.Team;
import db.TeamDao;
import de.greenrobot.dao.query.Query;
import timber.log.Timber;

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

        loadTeams();
    }

    /**
     * load Teams from DB and put them into spinner
     */
    public void loadTeams() {
        spinner = (Spinner) findViewById(R.id.teams);
        ArrayList<String> teams_array = new ArrayList<>();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        TeamDao teamDao = daoSession.getTeamDao();
        Query query = teamDao.queryBuilder().build();

        String query2 = "SELECT Name FROM Team";
        Cursor cursor = db.rawQuery(query2, null);

        if (cursor.moveToFirst()) {
            do {
                teams_array.add(cursor.getString(0));
                System.out.println(cursor.getString(0));
            } while (cursor.moveToNext());
            db.close();
        }

        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dropDown.addAll(teams_array);
        spinner.setAdapter(dropDown);
    }

    /**
     * Auto erstellen
     * @param view
     */
    public void createCar(View view){
        int teamID = 0; // TODO: load team ID into this var

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        carDao = daoSession.getCarDao();

        EditText editText = (EditText) findViewById(R.id.editText);
        String carName = editText.getText().toString();
        Car car = new Car(null, carName, teamID);
        long carID = carDao.insert(car);

        // Logging
        Timber.e("Created Car with ID: %s", carID);
        Timber.e("Created Car with Name: %s", car.getName());
        Timber.e("Created Car for Team: %s", car.getTeamID());

        finish();
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
