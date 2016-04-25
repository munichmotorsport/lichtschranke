package munichmotorsport.photocellapplication.gui;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.Car;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.LapDao;
import db.Team;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.model.Lap_Driven;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.Data;
import munichmotorsport.photocellapplication.utils.LapClickListener;
import munichmotorsport.photocellapplication.utils.LapTableDataAdapter;
import timber.log.Timber;

public class RaceTable extends AppCompatActivity {

    private DaoFactory factory;
    private TableView tableView;
    private long raceId;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;
    private Timer timer;
    private Boolean pollRunning = false;
    private Button btn_togglePoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        setTitle("Current Race");



        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        raceId = b.getLong("RaceID");
        Timber.e("RaceID: %s", raceId);
        setContentView(R.layout.activity_race_table);
        factory = new DaoFactory(this);
        tableView = (TableView) findViewById(R.id.laps);
        btn_togglePoll = (Button) findViewById(R.id.btn_togglePoll);
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
        tableView.addDataClickListener(new LapClickListener(this));
        toggleButtonText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    /**
     * fills the tableView with Lap Times from the DB
     */
    public void fillTable() {
        factory.initializeDB();
        int index = 0;

     /*   Timber.e("All finished Races: ");
        for (int i = 0; i < races.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s , mit Anzahl Runden: %s", races.get(i).getDescription(), races.get(i).getType(), races.get(i).getFinished().toString(), races.get(0).getLapList().size());
      }*/

        List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(
                LapDao.Properties.Time.isNotNull(),
                LapDao.Properties.RaceID.eq(raceId),
                LapDao.Properties.Number.notEq(0))
                .list();
        String[][] data = new String[laps.size()][6];

        if (laps.size() != 0) {
            Timber.e("laps in list: %s", laps.size());
            for (Lap l : laps) {
                for (int j = 0; j < 6; j++) {
                    switch (j) {
                        case 0:
                            List<Config> lapConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Id.eq(l.getConfigID())).list();
                            if (!lapConfig.isEmpty()) {
                                data[index][j] = lapConfig.get(0).getCar().getName();
                            } else {
                                data[index][j] = "Deleted Car";
                            }
                            break;
                        case 1:
                            data[index][j] = Integer.toString(l.getNumber());
                            break;
                        case 2:
                            String time = Long.toString(l.getTime().longValue());
                            data[index][j] = time;
                            break;
                        case 3:
                            data[index][j] = l.getId().toString();
                            break;
                        case 4:
                            data[index][j] = l.getDate();
                            break;
                        case 5:
                            data[index][j] = Long.toString(l.getConfigID());
                            break;
                    }
                }
                index++;
            }
        }

        String[][] sortedData = new String[data.length][6];
        int iterator = 0;
        for (int i = data.length - 1; i >= 0; i--) {
            sortedData[i] = data[iterator];
            iterator++;
        }

        tableView.setDataAdapter(new
                        LapTableDataAdapter(this, sortedData)
        );
        factory.closeDb();
    }

    public void togglePoll(View view) {
        togglePoll();
    }

    private void toggleButtonText() {
        if (pollRunning) {
            btn_togglePoll.setText("Pause");
        } else if (!pollRunning) {
            btn_togglePoll.setText("Start");
        }
    }

    /**
     *  toggle Poll
     */
    private void togglePoll () {
        if (pollRunning) {
            stopPoll();
            toggleButtonText();
        } else if (!pollRunning) {
            runPoll();
            toggleButtonText();
        }
    }

    /**
     *  stops the loop
     */
    private void stopPoll () {
        timer.cancel();
        pollRunning = false;
        Timber.e("Poll is paused");
    }

    /**
     *  runs the loop
     */
    private void runPoll() {
        timer = new Timer();
        timer.schedule(new Task(), 0, 1000);
        pollRunning = true;
        Timber.e("Poll is running");
    }

    class Task extends TimerTask {
        @Override
        public void run() {
            new HttpRequestTask().execute();
        }
    }

    class HttpRequestTask extends AsyncTask<Void, Void, Lap_Driven> {

        @Override
        protected Lap_Driven doInBackground(Void... params) {

            try {
                final String url = Data.url_getLaps;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Lap_Driven lapResponse = restTemplate.getForObject(url, Lap_Driven.class);
                return lapResponse;
            } catch (HttpClientErrorException e) {
                return null;
            } catch (ResourceAccessException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Lap_Driven lapResponse) {
            factory.initializeDB();
            if (lapResponse != null) {
                long timestamp = lapResponse.getTime();
                long time = 0;

                int barCode = lapResponse.getBarCode();

                List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Barcode.eq(barCode), ConfigDao.Properties.Current.eq(true)).list();
                long configId;
                if (config.size() > 0) {
                    configId = config.get(0).getId();
                } else {
                    Team dummyTeam = new Team(null, "Dummy Team");
                    long dummyTeamId = factory.getDao(DaoTypes.TEAM).insert(dummyTeam);
                    Car dummyCar = new Car(null, "Dummy Car", dummyTeamId);
                    long dummyCarId = factory.getDao(DaoTypes.CAR).insert(dummyCar);
                    Timber.e("Dummy Car ID: %s", dummyCarId);
                    Config dummyConfig = new Config(null, null, "" + barCode, null, true, dummyCarId);
                    configId = factory.getDao(DaoTypes.CONFIG).insert(dummyConfig);
                }

                List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(
                        LapDao.Properties.RaceID.eq(raceId),
                        LapDao.Properties.ConfigID.eq(configId))
                        .list();

                Timber.e("Anzahl Laps: %s in Rennen: %s, mit Config ID: %s", laps.size(), raceId, configId);

                int lapNumber = -1;
                Lap lastlap = null;
                for (int i = 0; i < laps.size(); i++) {
                    int actualNumber = laps.get(i).getNumber();
                    if (actualNumber > lapNumber) {
                        lapNumber = actualNumber;
                        lastlap = laps.get(i);
                    }
                }

                if (lastlap != null) {
                    time = timestamp - lastlap.getTimestamp();
                }

                calendar = Calendar.getInstance();
                String formattedDate = dateFormat.format(calendar.getTime());
                calendar.clear();

                Lap lapForDB = new Lap(null, formattedDate, timestamp, time, lapNumber + 1, raceId, configId);
                factory.getDao(DaoTypes.LAP).insert(lapForDB);
                factory.getDaoSession().clear();
                factory.closeDb();

                fillTable();

                Timber.e("Code: %s", barCode);
                Timber.e("TimeStamp: %s", timestamp);
                Timber.e("Time: %s", time);
            }
        }
    }

}
