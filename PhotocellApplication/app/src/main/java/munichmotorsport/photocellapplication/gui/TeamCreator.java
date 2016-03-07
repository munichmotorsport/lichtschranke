package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import db.Team;
import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;
import munichmotorsport.photocellapplication.utils.Utils;
import timber.log.Timber;

public class TeamCreator extends AppCompatActivity {

    private DaoFactory daoFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);
        setTitle("Neues Team erstellen");

        daoFactory = new DaoFactory(this);
    }

    /**
     * Team erstellen
     *
     * @param view
     */
    public void createTeam(View view) {
        EditText et_teamName = (EditText) findViewById(R.id.et_teamName);
        String teamName = et_teamName.getText().toString();

        if (Utils.nameCheck(teamName)) {
            Team team = new Team(null, teamName);
            long teamID = daoFactory.getDao(DaoTypes.TEAM).insert(team);

            finish();

            // Logging
            Timber.e("Created Team with ID: %s", teamID);
            Timber.e("Created Team with Name: %s", team.getName());
        }
    }

}
