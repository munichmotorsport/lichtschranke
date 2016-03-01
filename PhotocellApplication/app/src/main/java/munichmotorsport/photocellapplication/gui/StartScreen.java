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
import db.RaceDao;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class StartScreen extends AppCompatActivity {

    private DaoFactory factory;
    private Button newCar;
    private TextView currentRace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        factory = new DaoFactory(this);
        currentRace = (TextView) findViewById(R.id.currentRace);
        newCar = (Button) findViewById(R.id.btn_screen_createCar);
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
     * zur Activity "Rennen erstellen"
     * @param view
     */
    public void createRace(View view){
        Intent intent = new Intent(this, RaceCreator.class);
        startActivity(intent);
    }

    /**
     * zur Activity "Auto erstellen"
     * @param view
     */
    public void createCar(View view){
        Intent intent = new Intent(this, CarCreator.class);
        startActivity(intent);
    }

    public void showCurrentRace() {
        AbstractDao raceDa0 = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDa0.queryBuilder().list();
        if(races.isEmpty()) {
            newCar.setEnabled(false);
            newCar.setAlpha(0.5f);
        }
        else {
            newCar.setEnabled(true);
            newCar.setAlpha(1);
            currentRace.setText("Aktuelles Rennen: " + races.get(races.size() - 1).getDescription() + " (im Modus: " + races.get(races.size() - 1).getType() + ")");
        }

    }
}
