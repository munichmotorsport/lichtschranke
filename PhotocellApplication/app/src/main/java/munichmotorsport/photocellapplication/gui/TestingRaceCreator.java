package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import db.Car;
import db.CarDao;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.Race;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.Utils;
import timber.log.Timber;

public class TestingRaceCreator extends AppCompatActivity {
    EditText et_config;
    EditText et_weather;
    EditText et_driver;
    Spinner spn_cars;
    DaoFactory factory;
    ArrayAdapter<String> adapter;
    TextView tv_error;
    String description;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_race_creator);
        et_config = (EditText) findViewById(R.id.et_config);
        et_weather = (EditText) findViewById(R.id.et_weather);
        et_driver = (EditText) findViewById(R.id.et_driver);
        spn_cars = (Spinner) findViewById(R.id.spn_cars);
        tv_error = (TextView) findViewById(R.id.tv_error);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        factory = new DaoFactory(this);
        description = getIntent().getExtras().getString("raceDescription");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        List<Car> cars = factory.getDao(DaoTypes.CAR).queryBuilder().list();
        for (Car c : cars) {
            adapter.add(c.getName());
        }
        spn_cars.setAdapter(adapter);
        factory.closeDb();
    }

    @Override
    public void onResume() {
        super.onResume();
        factory.initializeDB();
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        for (Race t : races) {
            if (t.getFinished() == false) {
                Intent intent = new Intent(this, StartScreen.class);
                startActivity(intent);
            }
        }
        factory.closeDb();
    }

    public void createRace(View view) {
        factory.initializeDB();
        if (!Utils.nameCheck(et_config.getText().toString()) || !Utils.nameCheck(et_weather.getText().toString()) || spn_cars.getSelectedItem() == null || !Utils.nameCheck(et_driver.getText().toString())) {
            Timber.e("No config-, weather-, driver input or car selected");
            tv_error.setText("Please fill in all Informations for Testing ");
            factory.closeDb();
        } else {
            String carName = spn_cars.getSelectedItem().toString();
            List<Car> connectedCar = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Name.eq(carName)).list();
            List<Config> connectedConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(connectedCar.get(0).getId()), ConfigDao.Properties.Current.eq(true)).list();
            String comment = et_config.getText().toString();
            String driver = et_driver.getText().toString();
            Config oldConfig = connectedConfig.get(0);
            oldConfig.setCurrent(false);
            Config newConfig = new Config(null, comment, connectedConfig.get(0).getBarcode(), driver, true, connectedCar.get(0).getId());
            factory.getDao(DaoTypes.CONFIG).insertOrReplace(oldConfig);
            factory.getDao(DaoTypes.CONFIG).insert(newConfig);
            calendar = Calendar.getInstance();
            String formattedDate = dateFormat.format(calendar.getTime());
            calendar.clear();
            tv_error.setText("");
            Race race = new Race(null, "Testing", description, false, formattedDate, et_weather.getText().toString());
            long raceId = factory.getDao(DaoTypes.RACE).insert(race);
            Lap dummyLap = new Lap(null, null, null, null, -1, raceId, newConfig.getId(), connectedCar.get(0).getId());
            factory.getDao(DaoTypes.LAP).insert(dummyLap);

            Intent intent = new Intent(this, RaceTable.class);
            String[] raceInfo = new String[3];
            raceInfo[2] = Long.toString(raceId);
            intent.putExtra("RaceInfo", raceInfo);
            factory.closeDb();
            factory.getDaoSession().clear();
            startActivity(intent);
        }
    }
}
