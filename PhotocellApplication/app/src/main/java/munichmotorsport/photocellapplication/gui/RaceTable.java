package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.List;
import java.util.concurrent.TimeUnit;

import db.Lap;
import db.LapDao;
import db.Race;
import db.RaceDao;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.LapClickListener;
import munichmotorsport.photocellapplication.utils.LapTableDataAdapter;
import timber.log.Timber;

public class RaceTable extends AppCompatActivity {
    private long[] times = new long[100];
    private int[] laps = new int[100];
    private DaoFactory factory;
    private String car1 = "PWe7.16";
    private String car2 = "PWe6.15";
    private TableView tableView;
    private long raceId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        raceId = b.getLong("RaceID");
        Timber.e("RaceID: %s", raceId);
        setContentView(R.layout.activity_race_table);
        factory = new DaoFactory(this);
        tableView = (TableView) findViewById(R.id.laps);
        if (tableView == null) {
            Timber.e("tableview = null");
        } else {
            Timber.e(Integer.toString(tableView.getColumnCount()));
            String[] headers = {"Car", "Lap", "Time"};
            SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(this, headers);
            tableView.setHeaderAdapter(headerAdapter);
            tableView.setColumnWeight(0, 2);
            tableView.setColumnWeight(2, 2);
            fillTable();
        }
        tableView.addDataClickListener(new LapClickListener());
    }

    /**
     * fills the tableView with Lap Times from the DB
     */
    public void fillTable() {

        int index = 0;

        List<Race> existingRaces = factory.getDao(DaoTypes.RACE).queryBuilder().list();
        Timber.e("All Races in Database: ");
        for (int i = 0; i < existingRaces.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s", existingRaces.get(i).getDescription(), existingRaces.get(i).getType(), existingRaces.get(i).getFinished().toString());
        }


        List<Race> races = factory.getDao(DaoTypes.RACE).queryBuilder().where(RaceDao.Properties.Finished.eq(false)).list();

        Timber.e("All finished Races: ");
        for (int i = 0; i < races.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s , mit Anzahl Runden: %s", races.get(i).getDescription(), races.get(i).getType(), races.get(i).getFinished().toString(), races.get(0).getLapList().size());
        }

        List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(LapDao.Properties.Time.isNotNull(), LapDao.Properties.RaceID.eq(raceId)).list();
        String[][] data = new String[laps.size()][3];

        if (laps.size() != 0) {
            Timber.e("laps in list: %s", laps.size());
            for (Lap l : laps) {
                for (int j = 0; j < 3; j++) {
                    switch (j) {
                        case 0:
                            if(index != 0 && data[index-1][j] == car2) {
                                data[index][j] = car1;
                            }
                            else {
                                data[index][j] = car2;
                            }
                            break;
                        case 1:
                            data[index][j] = Integer.toString(l.getNumber());
                            break;
                        case 2:
                            String time = Long.toString(l.getTime().longValue());
                            data[index][j] = time;
                            break;
                    }
                }
                index++;
            }
        }

        tableView.setDataAdapter(new

                        LapTableDataAdapter(this, data)

        );
    }
}
