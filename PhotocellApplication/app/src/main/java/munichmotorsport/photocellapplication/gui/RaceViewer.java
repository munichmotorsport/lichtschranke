package munichmotorsport.photocellapplication.gui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Race;
import db.RaceDao;
import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.LapTableDataAdapter;
import munichmotorsport.photocellapplication.utils.RaceClickListener;
import timber.log.Timber;

public class RaceViewer extends AppCompatActivity {

    private ArrayAdapter<String> raceDescriptions;
    private DaoFactory factory;
    private TableView tv_races;
    private int RACEDESCRIPTION = 0;
    private int RACEDATE = 1;
    private int RACEID = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_viewer);
        setTitle("Races");

        factory = new DaoFactory(this);

        tv_races = (TableView)findViewById(R.id.tv_races);

        tv_races.addDataClickListener(new RaceClickListener(this));


        String[] headers = {"Description", "Date"};
        SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(this, headers);
        tv_races.setHeaderAdapter(headerAdapter);


        showExistingRaces();
    }

    @Override
    public void onResume(){
        super.onResume();
        showExistingRaces();
    }

    /**
     * Load existing Teams from DB
     */
    public void showExistingRaces() {
        factory.initializeDB();
        List<Race> races = factory.getDao(DaoTypes.RACE).queryBuilder().list();
        String[][] data = new String[races.size()][3];
        int index = 0;
        Timber.e("showExistingRaces()");
        for (Race r:races) {
            data[index][RACEDESCRIPTION] = r.getDescription();
            data[index][RACEDATE] = r.getDate();
            data[index][RACEID] = r.getId().toString();
            index ++;
        }
        tv_races.setDataAdapter(new SimpleTableDataAdapter(this, data));
        factory.getDaoSession().clear();
        factory.closeDb();
    }
}
