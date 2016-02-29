package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.gui.CreateCar;
import munichmotorsport.photocellapplication.gui.CreateRace;

public class StartScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        ImageView iv_backgroundimg = (ImageView) findViewById(R.id.iv_backgroundimg);
        iv_backgroundimg.setImageResource(R.drawable.logo_rw);
    }

    /**
     * zur Activity "Rennen erstellen"
     * @param view
     */
    public void createRace(View view){
        Intent intent = new Intent(this, CreateRace.class);
        startActivity(intent);
    }

    /**
     * zur Activity "Auto erstellen"
     * @param view
     */
    public void createCar(View view){
        Intent intent = new Intent(this, CreateCar.class);
        startActivity(intent);
    }

}
