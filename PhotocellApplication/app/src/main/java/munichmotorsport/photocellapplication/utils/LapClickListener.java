package munichmotorsport.photocellapplication.utils;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import timber.log.Timber;

/**
 * Created by jonas on 01.04.16.
 */
public class LapClickListener implements TableDataClickListener<String[]> {
    @Override
    public void onDataClicked(int rowIndex, String[] clickedLap) {
        Timber.e(clickedLap[2]);
    }
}
