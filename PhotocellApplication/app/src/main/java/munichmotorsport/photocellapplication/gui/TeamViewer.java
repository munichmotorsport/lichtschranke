package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Team;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class TeamViewer extends AppCompatActivity {

    private ArrayAdapter<String> teamNames;
    private DaoFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_viewer);
        setTitle("Teams");

        teamNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        factory = new DaoFactory(this);

        showExistingTeams();
    }

    @Override
    public void onResume(){
        super.onResume();
        showExistingTeams();
    }

    /**
     * Load existing Teams from DB
     */
    public void showExistingTeams() {
        factory.initializeDB();
        Timber.e("showExistingTeams()");
        teamNames.clear();
        AbstractDao teamDao = factory.getDao(DaoTypes.TEAM);
        List<Team> teams = teamDao.queryBuilder().list();
        for (int i = 0; i < teams.size(); i++) {
            teamNames.add(teams.get(i).getName());
            Timber.e("Found Team: %s", teams.get(i).getName());
        }
        ListView listView = (ListView) findViewById(R.id.lv_teams);
        listView.setAdapter(teamNames);
        factory.getDaoSession().clear();
        factory.closeDb();
    }

    /**
     * To activity "CarCreator"
     */
    public void createCar(View view) {
        Intent intent = new Intent(this, TeamCreator.class);
        startActivity(intent);
    }

}
