package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class CarManager extends AppCompatActivity {

    private DaoFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_manager);
        setTitle("Car Manager");
        factory = new DaoFactory(this);
    }

    /**
     * To activity "CarCreator"
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

    /**
     * Löscht alle Autos aus der Datenbank, nur zum Testen
     *
     * @param view
     */
    public void deleteCars(View view) {
        factory.getDao(DaoTypes.CAR).deleteAll();
    }
}