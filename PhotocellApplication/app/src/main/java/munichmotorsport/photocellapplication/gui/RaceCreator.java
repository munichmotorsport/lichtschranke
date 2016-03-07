package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Date;

import db.Race;

import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;


public class RaceCreator extends AppCompatActivity {

    private Spinner spn_modus;
    private DaoFactory factory;
    private long RaceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_race);
        setTitle("Neues Rennen erstellen");

        factory = new DaoFactory(this);

        spn_modus = (Spinner) findViewById(R.id.spn_modus);
        ArrayList<String> modi = new ArrayList<>();
        modi.add("Acceleration");
        modi.add("Auto Cross");
        modi.add("Endurance");
        modi.add("Skit Pad");
        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        dropDown.addAll(modi);
        spn_modus.setAdapter(dropDown);
    }

    /**
     * create a race and write it into DB
     *
     * @param view
     */
    public void createRace(View view) {
        Date date = new Date();
        String type = spn_modus.getSelectedItem().toString();

        EditText et_description = (EditText) findViewById(R.id.et_description);
        String description = et_description.getText().toString();
        Race race = new Race(null, type, description, false, date);
        RaceID = factory.getDao(DaoTypes.RACE).insert(race);
        // Logging
        Timber.e("Created Race with ID: %s, Description: '%s', Modus: %s, Date: %s", RaceID, race.getDescription(), race.getType(), date);

        toCarSelector();
    }

    /**
     * Switch to CarSelector Activity
     */
    private void toCarSelector() {
        Intent intent = new Intent(this, CarSelector.class);
        intent.putExtra("RaceCreator.RaceID", RaceID);
        startActivity(intent);
    }


}
