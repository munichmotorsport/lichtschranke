package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Car;
import db.CarDao;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class CarViewer extends AppCompatActivity {

    private ArrayAdapter<String> carNames;
    private DaoFactory factory;
    private ListView lv_cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_viewer);
        setTitle("View Cars");

        carNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        factory = new DaoFactory(this);

        showExistingCars();

        lv_cars = (ListView) findViewById(R.id.lv_cars);
        lv_cars.setClickable(true);
        lv_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                String carName = (String) lv_cars.getItemAtPosition(position);
                List<Car> carClicked = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Name.eq(carName)).list();
                Intent intent = new Intent(CarViewer.this, CarSettings.class);
                intent.putExtra("CarID", carClicked.get(0).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        showExistingCars();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showExistingCars();
    }

    /**
     * Load existing Cars from DB
     */
    public void showExistingCars() {
        Timber.e("showExistingCars()");
        carNames.clear();
        AbstractDao carDao = factory.getDao(DaoTypes.CAR);
        List<Car> cars = carDao.queryBuilder().list();
        for (int i = 0; i < cars.size(); i++) {
            carNames.add(cars.get(i).getName());
        }
        ListView listView = (ListView) findViewById(R.id.lv_cars);
        listView.setAdapter(carNames);
        factory.getDaoSession().clear();
    }
}
