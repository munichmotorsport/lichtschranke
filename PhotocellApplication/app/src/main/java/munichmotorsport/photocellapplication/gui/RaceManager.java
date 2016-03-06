package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import munichmotorsport.photocellapplication.R;

public class RaceManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_manager);
        setTitle("Race Manager");
    }

    /**
     * go to Activity "RaceCreator"
     * @param view
     */
    public void toRaceCreator(View view) {
        Intent intent = new Intent(this, RaceCreator.class);
        startActivity(intent);
    }

    /**
     * go to Activity "RaceCreator"
     * @param view
     */
    public void toRaceViewer(View view) {
        Intent intent = new Intent(this, RaceViewer.class);
        startActivity(intent);
    }
}
