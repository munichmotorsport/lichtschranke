package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import db.Car;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class CarViewer extends AppCompatActivity {

    private ArrayAdapter<String> carNames;
    private DaoFactory daoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_viewer);
        setTitle("View Cars");

        carNames = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        daoFactory = new DaoFactory(this);

        showExistingCars();
    }

    /**
     * Bereits vorhandene Autos im ListView anzeigen
     */
    public void showExistingCars() {
        carNames.clear();
        AbstractDao carDao = daoFactory.getDao(DaoTypes.CAR);
        List<Car> cars = carDao.queryBuilder().list();
        for(int i = 0; i < cars.size(); i++){
            carNames.add(cars.get(i).getName());
        }
        ListView listView = (ListView) findViewById(R.id.carList);
        listView.setAdapter(carNames);
    }
}
