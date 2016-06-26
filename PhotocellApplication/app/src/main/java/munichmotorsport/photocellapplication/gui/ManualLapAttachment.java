package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;


import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import db.Car;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.LapDao;
import db.Race;
import db.RaceDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.model.LapDto;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.Data;
import timber.log.Timber;


public class ManualLapAttachment extends AppCompatActivity {

    private ArrayAdapter<Long> timestampsAdapter;
    private List<Long> timestamps = new ArrayList<>();
    private Boolean pollRunning = false;
    private Button btn_togglePoll;
    private Timer timer;
    DaoFactory factory;
    ListView lv;
    long raceId;
    Race race;
    long time;
    long timestamp;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_lap_attachment);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_cars);
        lv = (ListView) findViewById(R.id.lv_timestamps);
        timestampsAdapter= new ArrayAdapter<Long>(this, android.R.layout.simple_list_item_1);

        factory = new DaoFactory(this);
        factory.initializeDB();
        List<Car> listOfCars = factory.getDao(DaoTypes.CAR).loadAll();
        raceId = getIntent().getLongExtra("raceId", 0);
        btn_togglePoll = (Button)findViewById(R.id.btn_togglePoll);

        List<Race> currentRace = factory.getDao(DaoTypes.RACE).queryBuilder().where(RaceDao.Properties.Id.eq(raceId)).list();
        race = currentRace.get(0);
        toggleButtonText();

        for (Car car : listOfCars) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            Button btn = new Button(this);
            final long carId =  car.getId();
            btn.setText(car.getName());
            btn.setBackgroundColor(Color.rgb(70, 80, 90));
            ll.addView(btn, params);
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                        addTimeToLap(carId);
                }
            });
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
        Intent intent = new Intent(this, RaceSettings.class);
        startActivity(intent);
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
                timestamp = lapResponse.getTime();
                time = 0;
                timestamps.add(timestamp);
                fillTimestamps();
            }
        }
    }

        public void fillTimestamps(){
            timestampsAdapter.clear();
            timestampsAdapter.addAll(timestamps);
            lv.setAdapter(timestampsAdapter);
        }

        public void addTimeToLap(long carId){
            Timber.e("CarId: %s", carId);
            List<Lap> lapsOfCarInRace = factory.getDao(DaoTypes.LAP).queryBuilder().where(
                    LapDao.Properties.RaceID.eq(raceId),
                    LapDao.Properties.CarID.eq(carId))
                    .list();
            List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(carId), ConfigDao.Properties.Current.eq(true)).list();
            Lap lapForDB;
            long configId = config.get(0).getId();
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

            if (time > 0) {
                lapForDB = new Lap(null, formattedDate, timestamp, time, lapNumber + 1, raceId, configId, carId);
            } else {
                lapForDB = new Lap(null, formattedDate, timestamp, null, lapNumber + 1, raceId, configId, carId);
            }

            factory.getDao(DaoTypes.LAP).insert(lapForDB);
            factory.getDaoSession().clear();
            factory.closeDb();

        }
}