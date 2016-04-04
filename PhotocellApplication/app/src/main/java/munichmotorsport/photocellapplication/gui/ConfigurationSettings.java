package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import db.Config;
import db.ConfigDao;
import db.DaoSession;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class ConfigurationSettings extends AppCompatActivity {

    private EditText et_comment;
    private DaoFactory factory;
    private long carId;
    private String comment;
    List<Config> currentConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration_settings);
        Bundle b = getIntent().getExtras();
        carId = b.getLong("CarID");
        factory = new DaoFactory(this);
        currentConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.CarID.eq(carId)).list();
        et_comment = (EditText) findViewById(R.id.et_comment);
        Timber.e("current config size: %s", currentConfig.size());
        if (!currentConfig.get(0).getComment().isEmpty()) {
            et_comment.setHint(currentConfig.get(0).getComment());
        }
        else{
            et_comment.setHint("No Confiruation comment set");
        }
    }

    public void changeComment(View view) {
        comment = et_comment.getText().toString();
        currentConfig.get(0).setComment(comment);
        factory.getDao(DaoTypes.CONFIG).update(currentConfig.get(0));
        et_comment.setHint(comment);
        et_comment.setText("");
        factory.getDaoSession().clear();
    }

    public void changeBarcode(View view) {
        Intent intent = new Intent(this, BarcodeChanger.class);
        startActivity(intent);
    }

    public void changeDriver(View view) {
        Intent intent = new Intent(this, DriverChanger.class);
        startActivity(intent);
    }
}
