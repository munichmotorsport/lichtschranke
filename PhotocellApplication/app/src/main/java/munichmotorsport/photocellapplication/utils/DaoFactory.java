package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import db.DaoMaster;
import db.DaoSession;
import de.greenrobot.dao.AbstractDao;
import timber.log.Timber;

public class DaoFactory {

    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private AbstractDao dao;

    DaoMaster.DevOpenHelper helper;

    public DaoFactory(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "photocell_db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    /**
     * creates a dao for writing and reading operations on the DB
     * @param daoType
     * @return matching dao for the input type
     */
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
            case CONFIG:
                dao = daoSession.getConfigDao();
                break;
            case LAP:
                dao = daoSession.getLapDao();
        }
        return dao;
    }

    /**
     * reach the daosession from the used factory
     * @return matching daoSession
     */
    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void initializeDB() {

        if (db == null) {
            db = helper.getWritableDatabase();
            Timber.e("initializeDB->: db==null");
        } else {
            if (!db.isOpen()) {
                db = helper.getWritableDatabase();
                Timber.e("initializeDB->: db!=null && !db.isOpen()");
            } else {
                Timber.e("initializeDB->: db!=null && db.isOpen()");
            }
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }

    public void closeDb() {
        db.close();
    }

}
