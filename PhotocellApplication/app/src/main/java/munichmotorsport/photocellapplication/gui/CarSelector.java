package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.List;

import db.Car;
import db.Config;
import db.Lap;
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
     *  Load existing Cars from DB
     */
    public void showExistingCars() {
        carNames.clear();
        AbstractDao carDao = daoFactory.getDao(DaoTypes.CAR);
        List<Car> cars = carDao.queryBuilder().list();
        for (int i=0; i<cars.size(); i++) {
            CheckBox cb_newCar = new CheckBox(this);
            cb_newCar.setText(cars.get(i).getName());
            cb_newCar.setId(cars.get(i).getId().intValue());
            ll_cars.addView(cb_newCar);
        }
    }

    /**
     * gets all selected cars
     *
     * @param view
     */
    public void getTickledBoxesAndSave(View view) {
        for (int i=0; i<ll_cars.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) ll_cars.getChildAt(i);
            if (checkBox.isChecked()) {
                addCarsToRace(checkBox.getId());
            }
        }
    }

    /**
     * connects a car to the current race and saves into DB
     *
     * @param carID
     */
    public void addCarsToRace(int carID) {
        Timber.e("Connecting Car %s to current Race %s", carID, RaceID);

        // Config(Long id, Integer Barcode, String Comment, long carID)
        Config config = new Config(null, null, null, carID);
        long configID = daoFactory.getDao(DaoTypes.CONFIG).insert(config);
        Timber.e("Created Config with ID: %s", configID);

        // Lap(Long id, long Time, int Number, long raceID, long configID)
        Lap lap = new Lap(null, null, 1, RaceID, configID);
        long lapID = daoFactory.getDao(DaoTypes.LAP).insert(lap);
        Timber.e("Created Lap with ID: %s", lapID);

        toRaceStarter(getCurrentFocus());
    }

    /**
     * zur Activity "RaceStarter"
     *
     * @param view
     */
    public void toRaceStarter(View view) {
        Intent intent = new Intent(this, RaceStarter.class);
        intent.putExtra("CarSelector.RaceID", RaceID);
        startActivity(intent);
    }
}

