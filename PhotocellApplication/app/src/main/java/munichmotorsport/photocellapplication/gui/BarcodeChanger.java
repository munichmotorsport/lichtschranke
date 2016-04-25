package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import db.Config;
import db.ConfigDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class BarcodeChanger extends AppCompatActivity {

    private DaoFactory factory;
    private long configId;
    private EditText et_barCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_changer);
        setTitle("set BarCode");

        factory = new DaoFactory(this);
        et_barCode = (EditText) findViewById(R.id.et_barCode);
        Bundle b = getIntent().getExtras();
        configId = b.getLong("ConfigId");
    }



    public void setBarCode(View view) {
        factory.initializeDB();
        String barCode = et_barCode.getText().toString();
        List<Config> config = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Id.eq(configId)).list();
        config.get(0).setBarcode(barCode);
        factory.getDao(DaoTypes.CONFIG).update(config.get(0));
        factory.closeDb();
    }
}
