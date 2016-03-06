package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private TextView tv_currentRace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        factory = new DaoFactory(this);
        tv_currentRace = (TextView) findViewById(R.id.currentRace);
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
     * zur Activity "TeamManager"
     *
     * @param view
     */
    public void teamManager(View view) {
        Intent intent = new Intent(this, TeamManager.class);
        startActivity(intent);
    }

    /**
     * Zeige aktuelles Rennen im TextView, ggf Button "CreateRace" deaktivieren
     */
    public void showCurrentRace() {
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        if (!races.isEmpty() && races.get(races.size() - 1).getFinished() == false) {
            tv_currentRace.setText("Current race: " + races.get(races.size() - 1).getDescription() + " (in mod: " + races.get(races.size() - 1).getType() + ")");
        } else {
            tv_currentRace.setText("No race used, please create one.");
        }
    }



}

