package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Car;
import db.Team;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class TeamViewer extends AppCompatActivity {

    private ArrayAdapter<String> teamNames;
    private DaoFactory daoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_viewer);
        setTitle("View Teams");

        teamNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        daoFactory = new DaoFactory(this);

        showExistingTeams();
    }

    /**
     *  Load existing Teams from DB
     */
    public void showExistingTeams() {
        Timber.e("showExistingTeams()");
        teamNames.clear();
        AbstractDao teamDao = daoFactory.getDao(DaoTypes.TEAM);
        List<Team> teams = teamDao.queryBuilder().list();
        for(int i = 0; i < teams.size(); i++){
            teamNames.add(teams.get(i).getName());
            Timber.e("Found Team: %s", teams.get(i).getName());
        }
        ListView listView = (ListView) findViewById(R.id.lv_teams);
        listView.setAdapter(teamNames);
    }
}
