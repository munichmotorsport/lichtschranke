package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import db.Car;
import db.DaoMaster;
import db.Team;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.Utils;
import timber.log.Timber;

public class CarCreator extends AppCompatActivity {

  //  private SQLiteDatabase db;
    private Spinner spn_teams;
    private EditText et_carName;
    private ArrayList<String> teamList_names;
    private ArrayList<Long> teamList_Ids;
    private ArrayAdapter<String> teamNames;
    private ArrayAdapter<String> carNames;
    private DaoFactory daoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        setTitle("Neues Auto erstellen");


        // elements
        spn_teams = (Spinner) findViewById(R.id.spn_teams);
        et_carName = (EditText) findViewById(R.id.et_carName);
        teamList_names = new ArrayList<>();
        teamList_Ids = new ArrayList<>();
        teamNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        carNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);


        // database
        daoFactory = new DaoFactory(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        //db = helper.getWritableDatabase();

        showExistingCars();
    }

    @Override
    protected void onResume () {
        super.onResume();
        resetTeamLists();
        //showExistingCars();
        Timber.e("onResume() called");
    }

    /**
     *  reset Teamlists
     */
    private void resetTeamLists() {
        teamNames.clear();
        teamList_names.clear();
        teamList_Ids.clear();
        Timber.e("Lists cleared, reload teams");
        loadTeams();
    }

    /**
     * load Teams from DB and put them into spinner
     */
    public void loadTeams() {
       AbstractDao teamDao = daoFactory.getDao(DaoTypes.TEAM);
        List<Team> teams = teamDao.queryBuilder().list();
        for(int i = 0; i < teams.size(); i++){
            teamList_names.add(teams.get(i).getName());
            teamList_Ids.add(teams.get(i).getId());
            Timber.e("Loaded Team with ID: %s, Name: %s", teams.get(i).getId(), teams.get(i).getName());
        }

       /* String query2 = "SELECT _id, Name FROM Team ORDER BY Name ASC";
        Cursor cursor = db.rawQuery(query2, null);

        if (cursor.moveToFirst()) {
            do {
                teamList_Ids.add(cursor.getLong(0));
                teamList_names.add(cursor.getString(1));
                Timber.e("Loaded Team with ID: %s, Name: %s", cursor.getLong(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();*/

        teamNames.addAll(teamList_names);
        spn_teams.setAdapter(teamNames);
    }

    /**
     * Auto erstellen
     * @param view
     */
    public void createCar(View view){
        int position = spn_teams.getSelectedItemPosition();
        String carName = et_carName.getText().toString();

        if (Utils.nameCheck(carName) && position >= 0) {
            long teamID = teamList_Ids.get(position);
            Car car = new Car(null, carName, teamID);
            long carID = daoFactory.getDao(DaoTypes.CAR).insert(car);

            // Logging
            Timber.e("Created Car with ID: %s", carID);
            Timber.e("Created Car with Name: %s", car.getName());
            Timber.e("Created Car for Team: %s", car.getTeamID());
            //vielleicht noch den Teamnamen loggen

         //   db.close();
            showExistingCars();
            finish();
        }
    }

    /**
     * Bereits vorhandene Autos im ListView anzeigen
     */
    public void showExistingCars() {
        AbstractDao carDao = daoFactory.getDao(DaoTypes.CAR);
        List<Car> cars = carDao.queryBuilder().list();
        for(int i = 0; i < cars.size(); i++){
            carNames.add(cars.get(i).getName());
        }
        ListView listView = (ListView) findViewById(R.id.carList);
        listView.setAdapter(carNames);
    }

    /**
     * zur Activity "Team erstellen"
     * @param view
     */
    public void createTeam(View view){
        Intent intent = new Intent(this, TeamCreator.class);
        startActivity(intent);
    }

}
