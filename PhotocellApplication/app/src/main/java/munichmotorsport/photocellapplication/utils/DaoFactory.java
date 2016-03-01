package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import db.DaoMaster;
import db.DaoSession;
import de.greenrobot.dao.AbstractDao;

public class DaoFactory {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AbstractDao dao;

    public DaoFactory(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public AbstractDao getDao(DaoTypes daoType) {
        switch (daoType) {
            case RACE:
                dao = daoSession.getRaceDao();
                break;
            case CAR:
                dao = daoSession.getCarDao();
                break;
            case TEAM:
                dao = daoSession.getTeamDao();
                break;
        }
        return dao;
    }

}
