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

    private EditText et_comment;
    private EditText et_driverName;
    private DaoFactory factory;
    private long carId;
    private String comment;
    private String driverName;
    private List<Config> currentConfig;
    private List<Car> currentCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_settings);

        Bundle b = getIntent().getExtras();
        carId = b.getLong("CarID");

        factory = new DaoFactory(this);
        currentCar = factory.getDao(DaoTypes.CAR).queryBuilder().where(CarDao.Properties.Id.eq(carId)).list();
        currentConfig = getCurrentConfig();
        et_comment = (EditText) findViewById(R.id.et_comment);
        et_driverName = (EditText) findViewById(R.id.et_driverName);

        Timber.e("current config size: %s", currentConfig.size());

        if (!currentConfig.get(0).getComment().isEmpty()) {
            et_comment.setHint(currentConfig.get(0).getComment());
        }
        else{
            et_comment.setHint("No Configuration comment set");
        }
        if(currentConfig.get(0).getDriver() != null && !currentConfig.get(0).getDriver().isEmpty()) {
            et_driverName.setHint(currentConfig.get(0).getDriver());
        }
        else{
            et_driverName.setHint("No driver set");
        }
    }

    /**
     * change config comment and update db
     * @param view
     */
    public void changeComment(View view) {
        comment = et_comment.getText().toString();
        Config newConfig = new Config(null, comment, null, currentConfig.get(0).getDriver(), true, currentCar.get(0).getId());
        currentConfig.get(0).setCurrent(false);
        factory.getDao(DaoTypes.CONFIG).update(currentConfig.get(0));
        factory.getDao(DaoTypes.CONFIG).insert(newConfig);
        et_comment.setHint(comment);
        et_comment.setText("");
        factory.getDaoSession().clear();
        currentConfig = getCurrentConfig();
    }

    /**
     * to activity "BarcodeChanger"
     * @param view
     */
    public void changeBarcode(View view) {
        Intent intent = new Intent(this, BarcodeChanger.class);
        startActivity(intent);
    }

    /**
     * change the driver of a car and update db
     * @param view
     */
    public void changeDriver(View view) {
        driverName = et_driverName.getText().toString();
        Config newConfig = new Config(null, currentConfig.get(0).getComment(), null, driverName, true, currentCar.get(0).getId());
        currentConfig.get(0).setCurrent(false);
        factory.getDao(DaoTypes.CONFIG).update(currentConfig.get(0));
        factory.getDao(DaoTypes.CONFIG).insert(newConfig);
        et_driverName.setHint(driverName);
        et_driverName.setText("");
        factory.getDaoSession().clear();
        currentConfig = getCurrentConfig();
    }

    private List<Config> getCurrentConfig() {
        List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(carId), ConfigDao.Properties.Current.eq(true)).list();
        factory.getDaoSession().clear();
        return config;
    }

}
