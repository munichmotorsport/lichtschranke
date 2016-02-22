package munichmotorsport.photocellapplication;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import db.DaoMaster;
import db.DaoSession;
import db.TeamDao;

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

    public void createTeam(View view){
        // TODO: Team in DB schreiben
        finish();
    }
}
