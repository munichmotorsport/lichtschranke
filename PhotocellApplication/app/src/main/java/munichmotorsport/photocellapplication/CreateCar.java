package munichmotorsport.photocellapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class CreateCar extends AppCompatActivity {

    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);
        setTitle("Neues Auto erstellen");

        spinner = (Spinner) findViewById(R.id.teams);

        ArrayList<String> teams = new ArrayList<>();
        // TODO: load teams
        // TODO: add teams to  teams
        teams.add("municHMotorsport");  // Bsp:
        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dropDown.addAll(teams);
        spinner.setAdapter(dropDown);
    }
}
