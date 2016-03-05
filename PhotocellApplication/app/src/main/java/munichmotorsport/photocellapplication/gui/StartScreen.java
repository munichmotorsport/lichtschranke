package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import db.Race;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class StartScreen extends AppCompatActivity {

    private DaoFactory factory;
    private Button btn_newRace;
    private Button btn_finishRace;
    private TextView tv_currentRace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        factory = new DaoFactory(this);
        tv_currentRace = (TextView) findViewById(R.id.currentRace);
        btn_newRace = (Button) findViewById(R.id.btn_screen_createRace);
        btn_finishRace = (Button) findViewById(R.id.finishRace);
        ImageView iv_backgroundimg = (ImageView) findViewById(R.id.iv_backgroundimg);
        iv_backgroundimg.setImageResource(R.drawable.logo_rw);

        showCurrentRace();
    }

    @Override
    public void onResume() {
        super.onResume();
        showCurrentRace();
    }

    /**
     * zur Activity "RaceManager"
     *
     * @param view
     */
    public void toRaceManager(View view) {
        Intent intent = new Intent(this, RaceManager.class);
        startActivity(intent);
    }

    /**
     * zur Activity "CarManager"
     *
     * @param view
     */
    public void carManager(View view) {
        Intent intent = new Intent(this, CarManager.class);
        startActivity(intent);
    }

    /**
     * Zeige aktuelles Rennen im TextView, ggf Button "CreateRace" deaktivieren
     */
    public void showCurrentRace() {
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        if (!races.isEmpty() && races.get(races.size() - 1).getFinished() == false) {
            btn_finishRace.setEnabled(true);
            btn_finishRace.setAlpha(1);
            btn_newRace.setEnabled(false);
            btn_newRace.setAlpha(0.5f);
            btn_newRace.setText("Current race has to be completed first. Please press 'Finish Current Race'");
            tv_currentRace.setText("Current race: " + races.get(races.size() - 1).getDescription() + " (in mod: " + races.get(races.size() - 1).getType() + ")");
        } else {

            btn_newRace.setEnabled(true);
            btn_newRace.setAlpha(1);
            btn_finishRace.setAlpha(0.5f);
            btn_finishRace.setEnabled(false);
            btn_newRace.setText("Race Manager");
            tv_currentRace.setText("No race used, please create one.");
        }
    }

    /**
     * Beendet aktuelles Rennen
     *
     * @param view
     */
    public void finishCurrentRace(View view) {
        AbstractDao dao = factory.getDao(DaoTypes.RACE);
        List<Race> races = dao.queryBuilder().list();
        Race race = races.get(races.size() - 1);
        race.setFinished(true);
        dao.insertOrReplace(race);
        Timber.e("All Races in Database: ");
        for(int i = 0; i < races.size(); i++) {
            Timber.e("Rennen: '%s' (Typ: %s) , finished: %s" , races.get(i).getDescription(), races.get(i).getType(), races.get(i).getFinished().toString());
        }
        showCurrentRace();
    }

}

