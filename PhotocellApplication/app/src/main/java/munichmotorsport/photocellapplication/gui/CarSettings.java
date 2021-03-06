package munichmotorsport.photocellapplication.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import db.Car;
import db.CarDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class CarSettings extends AppCompatActivity {

    private long carId;
    TextView et_carName;
    DaoFactory factory;
    List<Car> clickedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_settings);
        setTitle("Car Settings");
        factory = new DaoFactory(this);
        Bundle b = getIntent().getExtras();
        carId = b.getLong("CarID");
        Timber.e("Car ID: %s", carId);
        clickedCar = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Id.eq(carId)).list();
        et_carName = (EditText) findViewById(R.id.et_carName);
        et_carName.setHint(clickedCar.get(0).getName());
    }

    @Override
    protected void onResume(){
        super.onResume();
        factory.initializeDB();
        if(factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Id.eq(carId)).list().isEmpty()){
            Intent intent = new Intent(this, StartScreen.class);
            startActivity(intent);
        }
        factory.closeDb();
    }


    /**
     * go to activity ConfigurationSettings
     * @param view
     */
    public void toConfigOptions(View view) {
        Intent intent = new Intent(this, ConfigurationSettings.class);
        intent.putExtra("CarID", carId);
        startActivity(intent);
    }

    /**
     * go to activity TeamRelationModifier
     * @param view
     */
    public void changeTeamRelation(View view) {
        Intent intent = new Intent(this, TeamRelationModifier.class);
        intent.putExtra("CarID", carId);
        startActivity(intent);
    }

    /**
     * change the name of the car and update DB
     * @param view
     */
    public void changeCarName(View view) {
        factory.initializeDB();
        clickedCar.get(0).setName(et_carName.getText().toString());
        factory.getDao(DaoTypes.CAR).update(clickedCar.get(0));
        et_carName.setHint(et_carName.getText());
        et_carName.setText("");
        factory.closeDb();
    }

    /**
     * display a warning before delete
     * @param view
     */
    public void deleteCarWarning(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Reset Race")
                .setMessage("Are you sure you want to delete this car? It can't be undone.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCurrentCar();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * delete current car from db
     */
    public void deleteCurrentCar(){
        factory.initializeDB();
        factory.getDao(DaoTypes.CAR).delete(clickedCar.get(0));
        finish();
        Intent intent = new Intent(this, CarViewer.class);
        startActivity(intent);
        factory.closeDb();
    }
}
