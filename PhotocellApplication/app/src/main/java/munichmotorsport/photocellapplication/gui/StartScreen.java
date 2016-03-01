package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import db.Race;
import de.greenrobot.dao.AbstractDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class StartScreen extends AppCompatActivity {

    private DaoFactory factory;
    private Button newCar;
    private TextView currentRace;
    boolean click = true;
    PopupWindow popUp;



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

    /**
     * Zeige aktuelles Rennen im TextView, ggf Button "CreateCar" deaktivieren
     */
    public void showCurrentRace() {
        AbstractDao raceDao = factory.getDao(DaoTypes.RACE);
        List<Race> races = raceDao.queryBuilder().list();
        if(races.isEmpty()) {
            newCar.setEnabled(false);
            newCar.setAlpha(0.5f);
            currentRace.setText("Kein Rennen vorhanden, bitte erstellen Sie eines.");
        }
        else {
            newCar.setEnabled(true);
            newCar.setAlpha(1);
            currentRace.setText("Aktuelles Rennen: " + races.get(races.size() - 1).getDescription() + " (im Modus: " + races.get(races.size() - 1).getType() + ")");
        }
    }

    /**
     * noch in Entwicklung
     */
    public void showPopUp(){
        popUp = new PopupWindow(this);
        final RelativeLayout layout = (RelativeLayout) findViewById(R.id.popUp);
        Button but = new Button(this);
        but.setText("Click Me");
        but.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (click) {

                    popUp.showAtLocation(layout, layout.getGravity(), layout.getWidth(), layout.getHeight());
                    popUp.update(50, 50, 300, 80);
                    click = false;
                } else {
                    popUp.dismiss();
                    click = true;
                }
            }

        });
    }
}
