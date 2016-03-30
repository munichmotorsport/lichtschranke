package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.List;

import db.Lap;
import db.LapDao;
import db.Race;
import db.RaceDao;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.LapTableDataAdapter;
import timber.log.Timber;

public class RaceTable extends AppCompatActivity {
    private long[] times = new long[100];
    private int[] laps = new int[100];
    private DaoFactory factory;
    private String car = "PWe7.16";
    private TableView tableView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    /*    String[][] DATA_TO_SHOW = { { "This", "is", "a", "test" },
                { "and", "a", "second", "test" } };
        SimpleTableDataAdapter laps = new SimpleTableDataAdapter(this, DATA_TO_SHOW);
        tableView.setDataAdapter(laps);*/
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

        List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(LapDao.Properties.Time.isNotNull(), LapDao.Properties.RaceID.eq(races.get(races.size() - 1).getId())).list();
        String[][] data = new String[laps.size()][3];

        if (laps.size() != 0) {
            Timber.e("laps in list: %s", laps.size());
            for (Lap l : laps) {
                for (int j = 0; j < 3; j++) {
                    switch (j) {
                        case 0:
                            data[index][j] = car;
                            break;
                        case 1:
                            data[index][j] = Integer.toString(l.getNumber());
                            break;
                        case 2:
                            data[index][j] = Long.toString(l.getTime());
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
