package munichmotorsport.photocellapplication.gui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import munichmotorsport.photocellapplication.R;
import munichmotorsport.photocellapplication.utils.DaoFactory;
import munichmotorsport.photocellapplication.utils.DaoTypes;

public class TeamManager extends AppCompatActivity {

    private DaoFactory factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manager);
        setTitle("Team Manager");
        factory = new DaoFactory(this);
    }

    /**
     * Zur Activity "TeamCreator"
     *
     * @param view
     */
    public void goToTeamCreator(View view) {
        Intent intent = new Intent(this, TeamCreator.class);
        startActivity(intent);
    }

    /**
     * Zur Activity "TeamCreator"
     *
     * @param view
     */
    public void goToTeamViewer(View view) {
        Intent intent = new Intent(this, TeamViewer.class);
        startActivity(intent);
    }

    /**
     * Löscht alle Teams aus der Datenbank, nur zum Testen
     *
     * @param view
     */
    public void deleteTeams(View view) {
        factory.getDao(DaoTypes.TEAM).deleteAll();
    }
}
