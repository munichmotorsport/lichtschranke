package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Car;
import db.CarDao;
import db.Team;
import db.TeamDao;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.TeamArrayAdapter;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class TeamRelationModifier extends AppCompatActivity {

    private ArrayAdapter<String> teamNames;
    private DaoFactory factory;
    private ListView lv_teams;
    private long carId;
    List<Car> modifiedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_relation_modifier);
        factory = new DaoFactory(this);

        Bundle b = getIntent().getExtras();
        carId = b.getLong("CarID");

        modifiedCar = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Id.eq(carId)).list();

        teamNames = new TeamArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modifiedCar.get(0).getTeamID());


        lv_teams = (ListView) findViewById(R.id.lv_teams);
        lv_teams.setClickable(true);
        lv_teams.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String teamName = (String) lv_teams.getItemAtPosition(position);
                List<Team> teamClicked = factory.getDao(DaoTypes.TEAM).queryBuilder().where(TeamDao.Properties.Name.eq(teamName)).list();
                modifiedCar.get(0).setTeamID(teamClicked.get(0).getId());
                factory.getDao(DaoTypes.CAR).update(modifiedCar.get(0));
                Intent intent = new Intent(TeamRelationModifier.this, CarSettings.class);
                intent.putExtra("CarID", carId);
                factory.getDaoSession().clear();
                finish();
                startActivity(intent);
            }
        });
        showExistingTeams();
    }

    @Override
    protected void onResume() {
        super.onResume();
        teamNames = new TeamArrayAdapter<String>(this, android.R.layout.simple_list_item_1, modifiedCar.get(0).getTeamID());
        showExistingTeams();
    }

    /**
     * show existing teams in listview with current team grey
     */
    public void showExistingTeams() {
        int index = 0;
        Timber.e("showExistingTeams()");
        teamNames.clear();
        AbstractDao teamDao = factory.getDao(DaoTypes.TEAM);
        List<Team> teams = teamDao.queryBuilder().list();
        for (int i = 0; i < teams.size(); i++) {
            teamNames.add(teams.get(i).getName());
            Timber.e("Found Team: %s", teams.get(i).getName());
            if (teams.get(i).getId() == modifiedCar.get(0).getTeamID()) {
                index = i;
            }
        }
        ListView listView = (ListView) findViewById(R.id.lv_teams);
        listView.setAdapter(teamNames);
        factory.getDaoSession().clear();
    }
}
