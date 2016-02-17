package munichmotorsport.photocellapplication; /**
 * Created by jonas on 17.02.16.
 */

import android.app.Application;

import timber.log.Timber;

public class ApplicationClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Timber.i("Starting App!");
    }

}
