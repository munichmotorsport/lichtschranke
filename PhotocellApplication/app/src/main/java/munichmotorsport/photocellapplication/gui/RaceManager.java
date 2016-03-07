package munichmotorsport.photocellapplication.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

import db.LapDao;
import db.Race;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class RaceManager extends AppCompatActivity {
    DaoFactory factory;
    Button btn_finishRace;
    Button btn_newRace;
    Button btn_toCurrentRace;
    Button btn_resetCurrentRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_manager);
        setTitle("Race Manager");
        factory = new DaoFactory(this);
        btn_finishRace = (Button) findViewById(R.id.btn_finishRace);
        btn_newRace = (Button) findViewById(R.id.btn_toRaceCreator);
        btn_toCurrentRace = (Button) findViewById(R.id.btn_toCurrentRace);
        btn_resetCurrentRace = (Button) findViewById(R.id.btn_resetCurrentRace);
        manageButtons();
    }

    public void onResume(){
        super.onResume();
        manageButtons();
    }

    /**
     * go to Activity "RaceCreator"
     *
     * @param view
     */
    public void toRaceCreator(View view) {
        Intent intent = new Intent(this, RaceCreator.class);
        startActivity(intent);
    }

    /**
     * go to Activity "RaceCreator"
     *
     * @param view
     */
    public void toRaceViewer(View view) {
        Intent intent = new Intent(this, RaceViewer.class);
        startActivity(intent);
    }

    /**
     * finish the current race
     *
     * @param view
     */
    public void finishCurrentRace(View view) {
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        Race race = races.get(races.size() - 1);
        race.setFinished(true);
        raceDao.insertOrReplace(race);
        Timber.e("All Races in Database: ");
        for (int i = 0; i < races.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s", races.get(i).getDescription(), races.get(i).getType(), races.get(i).getFinished().toString());
        }
        manageButtons();
    }

    /**
     * change the look and availability of the buttons
     *
     */
    public void manageButtons() {
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        if (!races.isEmpty() && races.get(races.size() - 1).getFinished() == false) {
            btn_finishRace.setEnabled(true);
            btn_finishRace.setAlpha(1);
            btn_newRace.setEnabled(false);
            btn_newRace.setAlpha(0.5f);
            btn_toCurrentRace.setEnabled(true);
            btn_toCurrentRace.setAlpha(1);
            btn_resetCurrentRace.setEnabled(true);
            btn_resetCurrentRace.setAlpha(1);
            btn_newRace.setText("Current race has to be completed first. Please press 'Finish Current Race'");
        } else {
            btn_newRace.setEnabled(true);
            btn_newRace.setAlpha(1);
            btn_finishRace.setAlpha(0.5f);
            btn_finishRace.setEnabled(false);
            btn_toCurrentRace.setEnabled(false);
            btn_toCurrentRace.setAlpha(0.5f);
            btn_resetCurrentRace.setEnabled(false);
            btn_resetCurrentRace.setAlpha(0.5f);
            btn_newRace.setText("Create Race");
        }
    }

    public void toCurrentRace(View view){
        ;
    }

    public void resetCurrentRace (View view) {
        new AlertDialog.Builder(this)
                .setTitle("Reset Race")
                .setMessage("Are you sure you want to delete all measured lap times for this race?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
                        List<Race> races = raceDao.queryBuilder().list();
                        Race race = races.get(races.size() - 1);
                        AbstractDao lapDao = factory.getDao(DaoTypes.LAP);
                        lapDao.queryBuilder().where(LapDao.Properties.RaceID.eq(race.getId()));
                        manageButtons();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}


