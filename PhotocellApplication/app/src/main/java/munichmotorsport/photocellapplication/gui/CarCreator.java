package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

    private Spinner spn_teams;
    private EditText et_carName;
    private ArrayList<String> teamList_names;
    private ArrayList<Long> teamList_Ids;
    private ArrayAdapter<String> teamNames;
    private DaoFactory daoFactory;
    private TextView tv_error;

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
        tv_error = (TextView) findViewById(R.id.tv_error);

        // database
        daoFactory = new DaoFactory(this);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTeamLists();
    }

    /**
     * reset Teamlists
     */
    private void resetTeamLists() {
        teamNames.clear();
        teamList_names.clear();
        teamList_Ids.clear();
        Timber.e("Lists cleared, reload teams");
        loadTeams();
    }


    /**
     * load teams from DB and put them into spinner
     */
    public void loadTeams() {
        AbstractDao teamDao = daoFactory.getDao(DaoTypes.TEAM);
        List<Team> teams = teamDao.queryBuilder().list();
        for (int i = 0; i < teams.size(); i++) {
            teamList_names.add(teams.get(i).getName());
            teamList_Ids.add(teams.get(i).getId());
            Timber.e("Loaded Team with ID: %s, Name: %s", teams.get(i).getId(), teams.get(i).getName());
        }

        teamNames.addAll(teamList_names);
        spn_teams.setAdapter(teamNames);
    }

    /**
     * Create car and write it into DB
     *
     * @param view
     */
    public void createCar(View view) {
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

            finish();
            tv_error.setText("");
        } else {
            tv_error.setText("You have to select a team. If the matching team is missing, create it.");
        }
    }

    /**
     * to activity "TeamCreator"
     *
     * @param view
     */
    public void createTeam(View view) {
        Intent intent = new Intent(this, TeamCreator.class);
        startActivity(intent);
    }

}
