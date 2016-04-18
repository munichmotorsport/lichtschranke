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
import java.util.List;

import db.Race;

import de.greenrobot.dao.AbstractDao;
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
        setContentView(R.layout.activity_race_creator);
        setTitle("Neues Rennen erstellen");

        factory = new DaoFactory(this);

        spn_modus = (Spinner) findViewById(R.id.spn_modus);
        ArrayList<String> modi = new ArrayList<>();
        modi.add("Acceleration");
        modi.add("Auto Cross");
        modi.add("Endurance");
        modi.add("Skit Pad");
        modi.add("Testing");
        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        dropDown.addAll(modi);
        spn_modus.setAdapter(dropDown);
    }

    @Override
    public void onResume(){
        super.onResume();
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        for(Race t:races) {
            if(t.getFinished() == false){
                Intent intent = new Intent(this, StartScreen.class);
                startActivity(intent);
            }
        }
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
        if(type == "Testing") {
            Intent intent = new Intent(this, TestingRaceCreator.class);
            intent.putExtra("raceDescription", description);
            startActivity(intent);
        }
        else {
            Race race = new Race(null, type, description, false, date);
            RaceID = factory.getDao(DaoTypes.RACE).insert(race);
//            toCarSelector();
            toRaceTable();

            // Logging
            Timber.e("Created Race with ID: %s, Description: '%s', Modus: %s, Date: %s", RaceID, race.getDescription(), race.getType(), date);

        }
    }

    /**
     * Switch to CarSelector Activity
     */
    private void toCarSelector() {
        Intent intent = new Intent(this, CarSelector.class);
        intent.putExtra("RaceCreator.RaceID", RaceID);
        startActivity(intent);
    }

    /**
     * Switch to RaceTable Activity
     */
    private void toRaceTable() {
        Intent intent = new Intent(this, RaceTable.class);
        intent.putExtra("RaceID", RaceID);
        startActivity(intent);
    }


}
