package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import db.Car;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class TestingRaceCreator extends AppCompatActivity {
    EditText et_config;
    EditText et_weather;
    Spinner spn_cars;
    DaoFactory factory;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_race_creator);
        et_config = (EditText) findViewById(R.id.et_config);
        et_weather = (EditText) findViewById(R.id.et_weather);
        spn_cars = (Spinner) findViewById(R.id.spn_cars);
        factory = new DaoFactory(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        List<Car> cars = factory.getDao(DaoTypes.CAR).queryBuilder().list();
        for(Car c:cars){
            adapter.add(c.getName());
        }
        spn_cars.setAdapter(adapter);
    }

    public void createRace(View view) {


    }
}
