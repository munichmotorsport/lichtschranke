package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.List;

import db.Car;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.LapDao;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class CarSelector extends AppCompatActivity {

    private ArrayAdapter<String> carNames;
    private DaoFactory daoFactory;
    private LinearLayout ll_cars;
    private long RaceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selector);
        setTitle("Select Cars");
        Timber.e("Select Cars");

        ll_cars = (LinearLayout) findViewById(R.id.ll_cars);
        carNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        daoFactory = new DaoFactory(this);

        RaceID = getIntent().getLongExtra("RaceCreator.RaceID", RaceID);

        showExistingCars();
    }

    /**
     * Load existing Cars from DB
     */
    public void showExistingCars() {
        carNames.clear();
        AbstractDao carDao = daoFactory.getDao(DaoTypes.CAR);
        List<Car> cars = carDao.queryBuilder().list();
        for (int i = 0; i < cars.size(); i++) {
            CheckBox cb_newCar = new CheckBox(this);
            cb_newCar.setText(cars.get(i).getName());
            cb_newCar.setId(cars.get(i).getId().intValue());
            ll_cars.addView(cb_newCar);
        }
    }

    /**
     * load all selected cars
     *
     * @param view
     */
    public void getTickledBoxesAndSave(View view) {
        refreshDatabase();
        for (int i = 0; i < ll_cars.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) ll_cars.getChildAt(i);
            if (checkBox.isChecked()) {
                addCarsToRace(checkBox.getId());
            }
        }
        toRaceStarter(getCurrentFocus());
    }

    /**
     * delete existing Laps & Configs for this Race
     */
    public void refreshDatabase() {
        AbstractDao lapDao = daoFactory.getDao(DaoTypes.LAP);
        AbstractDao configDao = daoFactory.getDao(DaoTypes.CONFIG);

        List<Lap> lapsAlreadyCreated = lapDao.queryBuilder().where(LapDao.Properties.RaceID.eq(RaceID)).list();
        Timber.e("Size of already Created Laps for this race: %s", lapsAlreadyCreated.size());
        if (lapsAlreadyCreated.size() > 0) {
            for (int i = 0; i < lapsAlreadyCreated.size(); i++) {
                long configID = lapsAlreadyCreated.get(i).getConfigID();
                Timber.e("ConfigID to be deleted: %s", configID);
                List<Config> config = configDao.queryBuilder().where(ConfigDao.Properties.Id.eq(configID)).list();
                configDao.delete(config.get(0));
                lapDao.delete(lapsAlreadyCreated.get(i));
            }
        }
    }

    /**
     * connect a car to the current race and writes into DB
     *
     * @param carID
     */
    public void addCarsToRace(int carID) {
        Timber.e("Connecting Car %s to current Race %s", carID, RaceID);
/*
        // Config(Long id, String Comment, long carID)
        Config config = new Config(null, null, null, null, false, carID);
        long configID = daoFactory.getDao(DaoTypes.CONFIG).insert(config);
        Timber.e("Created Config with ID: %s", configID);

        // Lap(Long id, long Time, int Number, long raceID, long configID)
        Lap lap = new Lap(null, null,null, 1, RaceID, configID);
        long lapID = daoFactory.getDao(DaoTypes.LAP).insert(lap);
        Timber.e("Created Lap with ID: %s", lapID);
        */
    }

    /**
     * to activity "RaceStarter"
     *
     * @param view
     */
    public void toRaceStarter(View view) {
        Intent intent = new Intent(this, RaceStarter.class);
        intent.putExtra("CarSelector.RaceID", RaceID);
        startActivity(intent);
    }
}

