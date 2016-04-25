package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import db.Team;
import db.TeamDao;

/**
 * Created by jonas on 01.04.16.
 */
public class TeamArrayAdapter<String> extends ArrayAdapter<String> {

    long teamId;
    DaoFactory factory;

    public TeamArrayAdapter(Context context, int resource, long teamId) {
        super(context, resource);
        this.teamId = teamId;
        factory = new DaoFactory(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        factory.initializeDB();
        View v = super.getView(position, convertView, parent);
        List<Team> currentTeam = factory.getDao(DaoTypes.TEAM).queryBuilder().where(TeamDao.Properties.Name.eq(super.getItem(position))).list();
        if (currentTeam.get(0).getId() == teamId) {
            v.setEnabled(false);
            v.setAlpha(0.5f);
        } else {
            v.setEnabled(true);
            v.setAlpha(1);
        }
        factory.getDaoSession().clear();
        factory.closeDb();
        return v;
    }
}
