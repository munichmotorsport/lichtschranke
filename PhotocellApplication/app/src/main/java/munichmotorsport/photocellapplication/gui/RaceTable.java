package munichmotorsport.photocellapplication.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.Car;
import db.CarDao;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.LapDao;
import db.Race;
import db.RaceDao;
import db.Team;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.model.LapDto;
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
    private Button btn_deleteRace;
    private TextView tv_fastestLapInRace;
    private TextView tv_fastestLapForCar;
    private Race race;
    private String[][] content;
    private int CARNAME = 0;
    private int LAPNUMBER = 1;
    private int LAPTIME = 2;
    private int LAPID = 3;
    private int LAPDATE = 4;
    private int CONFIGID = 5;
    private String[] raceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        raceInfo = getIntent().getStringArrayExtra("RaceInfo");
        raceId = Long.parseLong(raceInfo[2]);

        Timber.e("RaceID: %s", raceId);
        setContentView(R.layout.activity_race_table);
        factory = new DaoFactory(this);

        tableView = (TableView) findViewById(R.id.laps);
        btn_togglePoll = (Button) findViewById(R.id.btn_togglePoll);
        btn_deleteRace = (Button) findViewById(R.id.btn_deleteRace);
        tv_fastestLapInRace = (TextView) findViewById(R.id.tv_fastestLapInRace);
        tv_fastestLapForCar = (TextView) findViewById(R.id.tv_fastestLapForCar);

        tv_fastestLapInRace.setTextColor(Data.fastestLapInRace);
        tv_fastestLapForCar.setTextColor(Data.fastestLapForCar);

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

        factory.initializeDB();
        List<Race> raceList = factory.getDao(DaoTypes.RACE).queryBuilder().where(RaceDao.Properties.Id.eq(raceId)).list();
        race = raceList.get(0);
        factory.closeDb();

        if(race.getFinished() == false) {
            btn_deleteRace.setEnabled(false);
        }

        setTitle(race.getDescription());

        if (race.getFinished()) {
            btn_togglePoll.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPoll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPoll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPoll();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, RaceViewer.class);
        startActivity(intent);
    }

    /**
     * fills the tableView with Lap Times from the DB
     */
    public void fillTable() {
        factory.initializeDB();
        int index = 0;

        List<Lap> laps = factory.getDao(DaoTypes.LAP).queryBuilder().where(
                LapDao.Properties.Time.isNotNull(),
                LapDao.Properties.RaceID.eq(raceId),
                LapDao.Properties.Number.notEq(0)).orderDesc(LapDao.Properties.Id)
                .list();
        String[][] data = new String[laps.size()][6];
        if (laps.size() != 0) {
            Timber.e("laps in list: %s", laps.size());
            for (Lap l : laps) {
                for (int j = 0; j < 6; j++) {
                    List<Config> lapConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Id.eq(l.getConfigID())).list();
                    if (!lapConfig.isEmpty() && lapConfig.get(0).getCar() != null) {
                        data[index][CARNAME] = lapConfig.get(0).getCar().getName();
                    } else {
                        data[index][CARNAME] = "Deleted Car";
                    }
                    data[index][LAPNUMBER] = Integer.toString(l.getNumber());
                    String time = Long.toString(l.getTime().longValue());
                    data[index][LAPTIME] = time;
                    data[index][LAPID] = l.getId().toString();
                    data[index][LAPDATE] = l.getDate();
                    data[index][CONFIGID] = Long.toString(l.getConfigID());
                }
                index++;
            }
        }

        content = data;

        tableView.setDataAdapter(new
                LapTableDataAdapter(this, data)
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
     * toggle Poll
     */
    private void togglePoll() {
        if (pollRunning) {
            stopPoll();
        } else if (!pollRunning) {
            runPoll();
        }
    }

    /**
     * stops the loop
     */
    private void stopPoll() {
        if (timer != null) {
            timer.cancel();
            pollRunning = false;
            toggleButtonText();
            Timber.e("Poll is paused");
        }
    }

    /**
     * runs the loop
     */
    private void runPoll() {
        if (!race.getFinished()) {
            timer = new Timer();
            timer.schedule(new Task(), 0, 1000);
            pollRunning = true;
            toggleButtonText();
            Timber.e("Poll is running");
        }
    }

    class Task extends TimerTask {
        @Override
        public void run() {
            new HttpRequestTask().execute();
        }
    }

    class HttpRequestTask extends AsyncTask<Void, Void, LapDto> {

        @Override
        protected LapDto doInBackground(Void... params) {

            try {
                final String url = Data.url_getLaps;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                LapDto lapResponse = restTemplate.getForObject(url, LapDto.class);
                return lapResponse;
            } catch (HttpClientErrorException e) {
                return null;
            } catch (ResourceAccessException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(LapDto lapResponse) {
            factory.initializeDB();
            if (lapResponse != null && !race.getFinished()) {
                long timestamp = lapResponse.getTime();
                long time = 0;
                int barCode = lapResponse.getBarCode();
                long configId;
                long carId;

                Timber.e("race mod: " + race.getType().toString());
                if (race.getType().toString().equals("Testing")) {
                    List<Lap> dummyLap = factory.getDao(DaoTypes.LAP).queryBuilder().where(LapDao.Properties.RaceID.eq(raceId), LapDao.Properties.Timestamp.isNull()).list();
                    carId = dummyLap.get(0).getCarID();
                    List<Race> currentRace = factory.getDao(DaoTypes.RACE).queryBuilder().where(RaceDao.Properties.Id.eq(raceId)).list();
                    List<Config> currentConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(carId), ConfigDao.Properties.Current.eq(true)).list();
                    configId = currentConfig.get(0).getId();
                    Timber.e("config car: " + currentConfig.get(0).getCar().getName());
                } else {
                    List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Barcode.eq(barCode), ConfigDao.Properties.Current.eq(true)).list();

                    if (config.size() == 1) {
                        configId = config.get(0).getId();
                        carId = config.get(0).getCarID();
                    } else {
                        Team dummyTeam = new Team(null, "Dummy Team");
                        long dummyTeamId = factory.getDao(DaoTypes.TEAM).insert(dummyTeam);
                        Car dummyCar = new Car(null, "Dummy Car", dummyTeamId);
                        long dummyCarId = factory.getDao(DaoTypes.CAR).insert(dummyCar);
                        Timber.e("Dummy Car ID: %s", dummyCarId);
                        Config dummyConfig = new Config(null, null, "" + barCode, null, true, dummyCarId);
                        configId = factory.getDao(DaoTypes.CONFIG).insert(dummyConfig);
                        carId = dummyCarId;
                    }
                }
                List<Lap> lapsOfCarInRace = factory.getDao(DaoTypes.LAP).queryBuilder().where(
                        LapDao.Properties.RaceID.eq(raceId),
                        LapDao.Properties.CarID.eq(carId))
                        .list();


                Timber.e("Anzahl Laps: %s in Rennen: %s, CarID: %s", lapsOfCarInRace.size(), raceId, carId);

                int lapNumber = -1;
                Lap lastlap = null;
                for (int i = 0; i < lapsOfCarInRace.size(); i++) {
                    int actualNumber = lapsOfCarInRace.get(i).getNumber();
                    if (actualNumber > lapNumber) {
                        lapNumber = actualNumber;
                        lastlap = lapsOfCarInRace.get(i);
                    }
                }

                if (lastlap != null && lastlap.getTimestamp() != null) {
                    time = timestamp - lastlap.getTimestamp();
                }

                calendar = Calendar.getInstance();
                String formattedDate = dateFormat.format(calendar.getTime());
                calendar.clear();

                writeLapIntoDb(formattedDate, timestamp, time, lapNumber, configId, carId);

                factory.getDaoSession().clear();
                factory.closeDb();

                fillTable();

                Timber.e("Code: %s", barCode);
                Timber.e("TimeStamp: %s", timestamp);
                Timber.e("Time: %s", time);
            }
        }

        public void writeLapIntoDb(String date, long timestamp, long time, int lapNumber, long configId, long carId){
            factory.initializeDB();
            String racetype = race.getDescription();
            Lap lapForDB;
            if(racetype == "Skit Pad") {

            }
            else{
                if (time > 0) {
                    lapForDB = new Lap(null, date, timestamp, time, lapNumber + 1, raceId, configId, carId);
                } else {
                    lapForDB = new Lap(null, date, timestamp, null, lapNumber + 1, raceId, configId, carId);
                }
                factory.getDao(DaoTypes.LAP).insert(lapForDB);
            }
            factory.getDaoSession().clear();
            factory.closeDb();
        }
    }

    /**
     * create a file containing the race informations
     * @param view
     */
    public void createFile(View view) {
        factory.initializeDB();

        String filename = race.getDescription() + "_" + race.getDate();
        String contentString = "Description: "+ race.getDescription() + "    Date: " + race.getDate() + "    Weather Condition: "+race.getWeather()+"\nCar             Lap       Time                 Date                          Config                Driver" + "\n";
        try {

            File root = new File(getExternalCacheDir(), "Races");

            if (!root.exists()) {
                root.mkdirs();
            }

            File filepath = new File(root, filename + ".txt");
            FileWriter writer = new FileWriter(filepath);

            for (int i = 0; i < content.length; i++) {
                List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Id.eq(Long.parseLong(content[i][5]))).list();
                contentString += content[i][0] + "   ||   " + content[i][1] + "   ||   " + Float.parseFloat(content[i][2]) / 1000.000 + "   ||   " + content[i][4] + "   ||   " + config.get(0).getComment() + "   ||   " +config.get(0).getDriver()+"\n";
            }

            writer.append(contentString);
            writer.flush();
            writer.close();

            String m = "File generated with name " + filename + ".txt in Folder: " + root.getAbsolutePath();
            Timber.e(m);


        } catch (IOException e) {
            e.printStackTrace();
        }
        factory.closeDb();
    }

    /**
     * delete chosen race
     */
    public void deleteRace(){
        factory.initializeDB();
        factory.getDao(DaoTypes.RACE).delete(race);
        finish();
        Intent intent = new Intent(this, RaceViewer.class);
        startActivity(intent);
    }

    /**
     * show alert Dialog about deleting a race
     * @param view
     */
    public void showAlertDialog(View view){
        new AlertDialog.Builder(this)
                .setTitle("Delete Race")
                .setMessage("Are you sure you want to delete the race? This cant be undone")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRace();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
