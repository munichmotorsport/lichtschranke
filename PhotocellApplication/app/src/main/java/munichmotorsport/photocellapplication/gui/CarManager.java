package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import munichmotorsport.photocellapplication.R;

public class CarManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        setTitle("Car Manager");
    }

    /**
     * zur Activity "CarCreator"
     *
     * @param view
     */
    public void carCreator(View view) {
        Intent intent = new Intent(this, CarCreator.class);
        startActivity(intent);
    }

    /**
     * zur Activity "CarViewer"
     *
     * @param view
     */
    public void carViewer(View view) {
        Intent intent = new Intent(this, CarViewer.class);
        startActivity(intent);
    }
}