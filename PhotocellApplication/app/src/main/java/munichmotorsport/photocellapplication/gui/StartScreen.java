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
import timber.log.Timber;

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
        factory.closeDb();
        showCurrentRace();
    }

    @Override
    public void onResume() {
        super.onResume();
        showCurrentRace();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
     * to activity "CarManager"
     *
     * @param view
     */
    public void carManager(View view) {
        Intent intent = new Intent(this, CarManager.class);
        startActivity(intent);
    }

    /**
     * to activity "TeamManager"
     *
     * @param view
     */
    public void teamManager(View view) {
        Intent intent = new Intent(this, TeamManager.class);
        startActivity(intent);
    }

    /**
     * show current race in textview
     */
    public void showCurrentRace() {
        factory.initializeDB();

        List<Race> races = factory.getDao(DaoTypes.RACE).queryBuilder().list();


        if (!races.isEmpty() && races.get(races.size() - 1).getFinished() == false) {
            tv_currentRace.setText("Current race: " + races.get(races.size() - 1).getDescription() + " (in mod: " + races.get(races.size() - 1).getType() + ")");
            Timber.e("Last Race: %s with status finished: %s", races.get(races.size() - 1).getDescription(), races.get(races.size() - 1).getFinished().toString());
        } else {
            tv_currentRace.setText("No race used, please create one.");
        }
        factory.getDaoSession().clear();
        factory.closeDb();
    }


}

