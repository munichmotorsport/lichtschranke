package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import db.Config;
import db.ConfigDao;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import timber.log.Timber;

public class LapViewer extends AppCompatActivity {

    String[] lapInfo;
    TextView tv_lapId;
    TextView tv_driver;
    TextView tv_lapNr;
    TextView tv_configComment;
    TextView tv_date;
    DaoFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_viewer);
        setTitle("View Lap");

        tv_lapId = (TextView)findViewById(R.id.tv_lapId);
        tv_lapNr = (TextView)findViewById(R.id.tv_lapNr);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_driver = (TextView)findViewById(R.id.tv_driver);
        tv_configComment = (TextView)findViewById(R.id.tv_configComment);

        factory = new DaoFactory(this);
        lapInfo = getIntent().getStringArrayExtra("LapInfo");

        tv_lapId.append(lapInfo[3]);
        tv_lapNr.append(lapInfo[1]);
        tv_date.append(lapInfo[4]);

        List<Config> drivenConfig = factory.getDao(DaoTypes.CONFIG).queryBuilder().where(ConfigDao.Properties.Id.eq(Long.parseLong(lapInfo[5]))).list();
        Timber.e("Driven Config: " +drivenConfig.get(0).getComment());
        tv_configComment.setText(drivenConfig.get(0).getComment());
        if(drivenConfig.get(0).getDriver() != null) {
            tv_driver.append(drivenConfig.get(0).getDriver());
        }
        factory.closeDb();
    }
}
