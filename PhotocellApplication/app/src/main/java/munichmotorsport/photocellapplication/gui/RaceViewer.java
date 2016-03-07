package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Race;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class RaceViewer extends AppCompatActivity {

    private ArrayAdapter<String> raceDescriptions;
    private DaoFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_viewer);
        setTitle("View Races");

        raceDescriptions = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        factory = new DaoFactory(this);

        showExistingRaces();
    }

    /**
     * Load existing Teams from DB
     */
    public void showExistingRaces() {
        Timber.e("showExistingRaces()");
        raceDescriptions.clear();
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        for (int i = 0; i < races.size(); i++) {
            raceDescriptions.add(races.get(i).getDescription());
            Timber.e("Found Race: %s finished: %s", races.get(i).getDescription(), races.get(i).getFinished().toString());
        }
        ListView listView = (ListView) findViewById(R.id.lv_races);
        listView.setAdapter(raceDescriptions);
        factory.getDaoSession().clear();
    }
}
