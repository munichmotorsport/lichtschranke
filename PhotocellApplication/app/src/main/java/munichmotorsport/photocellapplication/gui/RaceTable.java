package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.List;

import db.Lap;
import db.LapDao;
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
import timber.log.Timber;

public class RaceTable extends AppCompatActivity {
    long[] times = new long[100];
    int[] laps = new int[100];
    String[][] data = new String[100][100];
    DaoFactory factory;
    String car = "PWe7.16";
    TableView tableView;


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

    public void fillTable() {

        List<Race> existingRaces = factory.getDao(DaoTypes.RACE).queryBuilder().list();
        Timber.e("All Races in Database: ");
        for (int i = 0; i < existingRaces.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s", existingRaces.get(i).getDescription(), existingRaces.get(i).getType(), existingRaces.get(i).getFinished().toString());
        }


        List<Race> races = factory.getDao(DaoTypes.RACE).queryBuilder().where(RaceDao.Properties.Finished.eq(false)).list();

        Timber.e("All Races in races: ");
        for (int i = 0; i < races.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s", races.get(i).getDescription(), races.get(i).getType(), races.get(i).getFinished().toString());
        }

        List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(LapDao.Properties.RaceID.eq(races.get(races.size()-1).getId())).list();
        if(laps.size() != 0) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 3; j++) {
                    switch (j) {
                        case 0:
                            data[i][j] = car;
                            break;
                        case 1:
                            data[i][j] = Integer.toString(laps.get(i).getNumber());
                            break;
                        case 2:
                            data[i][j] = Long.toString(laps.get(i).getTime());
                    }
                }
            }
        }
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, data));
    }
    /*public void fillTable(){
        TableLayout maintable = (TableLayout)findViewById(R.id.maintable);
        TextView headerCar = (TextView) findViewById(R.id.car);
        headerCar.getLayoutParams().width = headerCar.getWidth();

        TableRow tr[]= new TableRow[40];
        TextView tv[] = new TextView[3];

        long time = 23000L;
        int lap = 1;
        String car = "PWe7.16";

        for (int n = 0; n < 40; n++) {
            times[n] = time;
            time++;
            cars[n] = car;
            laps[n] = lap;
            lap++;
            tr[n] = new TableRow(this);
            tr[n].setId(n);


            for (int j = 0; j < 3; j++) {
                tv[j] = new TextView(this);
                tv[j].setId(j);
                if(j == 0) {
                    tv[j].setText(cars[n]);
                }
                if(j == 1) {
                    tv[j].setText(Integer.toString(laps[n]));
                }
                if(j == 2) {
                    tv[j].setText(Long.toString(times[n]));
                }
                tr[n].addView(tv[j]);
            }
            maintable.addView(tr[n]);
        }
    }*/
}
