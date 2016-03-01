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

public class StartScreen extends AppCompatActivity {

    private DaoFactory factory;
    private Button newRace;
    private Button finishRace;
    private TextView currentRace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        factory = new DaoFactory(this);
        currentRace = (TextView) findViewById(R.id.currentRace);
        newRace = (Button) findViewById(R.id.btn_screen_createRace);
        finishRace = (Button) findViewById(R.id.finishRace);
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
            finishRace.setEnabled(true);
            finishRace.setAlpha(1);
            newRace.setEnabled(false);
            newRace.setAlpha(0.5f);
            newRace.setText("Current race has to be completed first. Please press 'Finish Current Race'");
            currentRace.setText("Current race: " + races.get(races.size() - 1).getDescription() + " (in mod: " + races.get(races.size() - 1).getType() + ")");
        } else {
            newRace.setEnabled(true);
            newRace.setAlpha(1);
//            newRace.setText("Create Race");
            currentRace.setText("No race used, please create one.");
        }
    }

    /**
     * Beendet aktuelles Rennen
     *
     * @param view
     */
    public void finishCurrentRace(View view) {
        List<Race> races = factory.getDao(DaoTypes.RACE).queryBuilder().list();
        races.get(races.size() - 1).setFinished(true);
        finishRace.setEnabled(false);
        finishRace.setAlpha(0.5f);
        showCurrentRace();
    }

}

