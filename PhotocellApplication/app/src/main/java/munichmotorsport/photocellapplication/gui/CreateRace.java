package munichmotorsport.photocellapplication.gui;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import db.DaoMaster;
import db.DaoSession;
import db.Race;
import db.RaceDao;

import munichmotorsport.photocellapplication.R;
import timber.log.Timber;


public class CreateRace extends AppCompatActivity {

    RaceDao raceDao;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    Spinner spn_modus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_race);
        setTitle("Neues Rennen erstellen");

        spn_modus = (Spinner) findViewById(R.id.spn_modus);
        ArrayList<String> modi = new ArrayList<>();
        modi.add("Acceleration");
        modi.add("Auto Cross");
        modi.add("Endurance");
        modi.add("Skit Pad");
        ArrayAdapter<String> dropDown = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dropDown.addAll(modi);
        spn_modus.setAdapter(dropDown);
    }

    /**
     * Rennen erstellen
     * @param view
     */
    public void createRace(View view){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        raceDao = daoSession.getRaceDao();

        String type = spn_modus.getSelectedItem().toString();

        EditText et_description = (EditText) findViewById(R.id.et_description);
        String description = et_description.getText().toString();
        Race race = new Race(null, description, type);
        long RaceID = raceDao.insert(race);

        // Logging
        Timber.e("Created Race with ID: %s", RaceID);
        Timber.e("Created Race with Description: %s", race.getDescription());
        Timber.e("Created Race with Modus: %s", race.getType());
    }
}
