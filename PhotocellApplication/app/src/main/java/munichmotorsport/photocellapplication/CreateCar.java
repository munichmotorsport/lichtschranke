package munichmotorsport.photocellapplication;

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

import db.Car;
import db.CarDao;
import db.DaoMaster;
import db.DaoSession;
import timber.log.Timber;

public class CreateCar extends AppCompatActivity {

    private CarDao carDao;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Spinner spinner;
    private EditText editText;
    private ArrayList<String> teamList_names;
    private ArrayList<Long> teamList_Ids;
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        setTitle("Neues Auto erstellen");

        // elements
        spinner = (Spinner) findViewById(R.id.teams);
        editText = (EditText) findViewById(R.id.editText);
        teamList_names = new ArrayList<>();
        teamList_Ids = new ArrayList<>();
        stringArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);

        // database
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        carDao = daoSession.getCarDao();
    }

    @Override
    protected void onResume () {
        super.onResume();
        resetTeamLists();
    }

    /**
     *  reset Teamlists
     */
    private void resetTeamLists() {
        stringArrayAdapter.clear();
        teamList_names.clear();
        teamList_Ids.clear();
        Timber.e("Lists cleared, reload teams");
        loadTeams();
    }

    /**
     * load Teams from DB and put them into spinner
     */
    public void loadTeams() {
        String query2 = "SELECT _id, Name FROM Team ORDER BY Name ASC";
        Cursor cursor = db.rawQuery(query2, null);

        if (cursor.moveToFirst()) {
            do {
                teamList_Ids.add(cursor.getLong(0));
                teamList_names.add(cursor.getString(1));
                Timber.e("Loaded Team with ID: %s, Name %s", cursor.getLong(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }

        stringArrayAdapter.addAll(teamList_names);
        spinner.setAdapter(stringArrayAdapter);
    }

    /**
     * Auto erstellen
     * @param view
     */
    public void createCar(View view){
        long teamID = teamList_Ids.get(spinner.getSelectedItemPosition());

        String carName = editText.getText().toString();
        Car car = new Car(null, carName, teamID);
        long carID = carDao.insert(car);

        // Logging
        Timber.e("Created Car with ID: %s", carID);
        Timber.e("Created Car with Name: %s", car.getName());
        Timber.e("Created Car for Team: %s", car.getTeamID());

        db.close();
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
