package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Car;
import db.CarDao;
import db.Config;
import db.ConfigDao;
import db.Lap;
import db.LapDao;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class RaceStarter extends AppCompatActivity {

    private ArrayAdapter<String> carNames;
    private DaoFactory daoFactory;
    private long raceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_starter);
        setTitle("Übersicht");

        carNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        daoFactory = new DaoFactory(this);

        raceID = getIntent().getLongExtra("CarSelector.RaceID", raceID);
        Timber.e("Übersicht");
        Timber.e("RaceID: %s", raceID);
        showCurrentCars();
    }

    public void showCurrentCars() {
        carNames.clear();
        AbstractDao lapDao = daoFactory.getDao(DaoTypes.LAP);

        List<Lap> laps = lapDao.queryBuilder().where(LapDao.Properties.RaceID.eq(raceID)).list();
        for (int i = 0; i < laps.size(); i++) {
            AbstractDao configDao = daoFactory.getDao(DaoTypes.CONFIG);
            long configID = laps.get(i).getConfigID();
            List<Config> config = configDao.queryBuilder().where(ConfigDao.Properties.Id.eq(configID)).list();
            Timber.e("ConfigID: %s", configID);

            long carID = config.get(0).getCarID();
            AbstractDao carDao = daoFactory.getDao(DaoTypes.CAR);
            List<Car> car = carDao.queryBuilder().where(CarDao.Properties.Id.eq(carID)).list();
            Timber.e("CarID: %s", carID);

            carNames.add(car.get(0).getName());
            Timber.e("carName added: %s", car.get(0).getName());
        }
        ListView listView = (ListView) findViewById(R.id.lv_currentCars);
        listView.setAdapter(carNames);
    }

}
