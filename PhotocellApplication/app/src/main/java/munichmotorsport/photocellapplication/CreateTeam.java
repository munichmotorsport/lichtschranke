package munichmotorsport.photocellapplication;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import db.DaoMaster;
import db.DaoSession;
import db.Team;
import db.TeamDao;
import timber.log.Timber;

public class CreateTeam extends AppCompatActivity {

    private TeamDao teamDao;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        setTitle("Neues Team erstellen");
    }

    /**
     * Team erstellen
     * @param view
     */
    public void createTeam(View view){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        teamDao = daoSession.getTeamDao();

        EditText editText = (EditText) findViewById(R.id.teamName);
        String teamName = editText.getText().toString();
        Team team = new Team(null, teamName);
        long teamID = teamDao.insert(team);

        Timber.e("Created Team with ID: %s", teamID);
        Timber.e("Created Team with Name: %s", team.getName());

        finish();
    }
}
