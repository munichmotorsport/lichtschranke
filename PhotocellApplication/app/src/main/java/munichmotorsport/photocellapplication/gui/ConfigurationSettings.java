package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import db.Car;
import db.CarDao;
import db.Config;
import db.ConfigDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class ConfigurationSettings extends AppCompatActivity {

    private EditText et_configComment;
    private EditText et_driverName;
    private EditText et_barcode;
    private DaoFactory factory;
    private long carId;
    private String comment;
    private String driverName;
    private String barcode;
    private List<Config> currentConfig;
    private List<Car> currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_settings);
        setTitle("Change Configuration");

        Bundle b = getIntent().getExtras();
        carId = b.getLong("CarID");

        factory = new DaoFactory(this);
        currentCar = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Id.eq(carId)).list();
        currentConfig = getCurrentConfig();
        et_configComment = (EditText) findViewById(R.id.et_configComment);
        et_driverName = (EditText) findViewById(R.id.et_driverName);
        et_barcode = (EditText) findViewById(R.id.et_barcode);

        Timber.e("current config size: %s", currentConfig.size());

        if (currentConfig.get(0).getComment() != null) {
            et_configComment.setText(currentConfig.get(0).getComment());
        }
        else{
            et_configComment.setHint("No Configuration comment set");
        }
        if(currentConfig.get(0).getDriver() != null) {
            et_driverName.setText(currentConfig.get(0).getDriver());
        }
        else{
            et_driverName.setHint("No driver set");
        }
        if(currentConfig.get(0).getBarcode() != null){
            et_barcode.setText(currentConfig.get(0).getBarcode());
        }
        else{
            et_barcode.setHint("No Barcode set");
        }
        factory.closeDb();
    }



    /**
     * change config comment and update db
     * @param view
     */
    /*
    public void changeComment(View view) {
        factory.initializeDB();
        comment = et_configComment.getText().toString();
        Config newConfig = new Config(null, comment, null, currentConfig.get(0).getDriver(), true, currentCar.get(0).getId());

        factory.getDao(DaoTypes.CONFIG).insert(newConfig);
        et_configComment.setText(comment);
        factory.getDaoSession().clear();
        factory.closeDb();
        currentConfig = getCurrentConfig();
        Timber.e("Current Config ID: %s", currentConfig.get(0).getId());
    }

    /**
     * to activity "BarcodeChanger"
     * @param view
     */
    /*
    public void changeBarcode(View view) {
        Intent intent = new Intent(this, BarcodeChanger.class);
        intent.putExtra("ConfigId", getCurrentConfig().get(0).getId());
        startActivity(intent);
    }

    /**
     * change the driver of a car and update db
     * @param view
     */
    /*
    public void changeDriver(View view) {
        factory.initializeDB();
        driverName = et_driverName.getText().toString();
        currentConfig.get(0).setCurrent(false);
        factory.getDao(DaoTypes.CONFIG).update(currentConfig.get(0));
        et_driverName.setText(driverName);
        factory.getDaoSession().clear();
        factory.closeDb();
        currentConfig = getCurrentConfig();
    }
*/

    /**
     * get currentConfig for edited car
     * @return
     */
    private List<Config> getCurrentConfig() {
        factory.initializeDB();
        List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(carId), ConfigDao.Properties.Current.eq(true)).list();
        factory.getDaoSession().clear();
        factory.closeDb();
        return config;
    }

    /**
     * write changes into db and update it
     * @param view
     */
    public void submitChanges(View view){
        factory.initializeDB();
        comment = et_configComment.getText().toString();
        barcode = et_barcode.getText().toString();
        driverName = et_driverName.getText().toString();
        currentConfig.get(0).setCurrent(false);
        factory.getDao(DaoTypes.CONFIG).insertOrReplace(currentConfig.get(0));
        Config newConfig = new Config(null, comment, barcode, driverName, true, currentCar.get(0).getId());
        factory.getDao(DaoTypes.CONFIG).insert(newConfig);
        factory.getDaoSession().clear();
        factory.closeDb();
        currentConfig = getCurrentConfig();
        Intent intent = new Intent(this, CarSettings.class);
        intent.putExtra("CarID", carId);
        startActivity(intent);
    }

}
