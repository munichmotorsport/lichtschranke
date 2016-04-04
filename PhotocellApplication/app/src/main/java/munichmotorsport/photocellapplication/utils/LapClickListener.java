package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import db.Lap;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import munichmotorsport.photocellapplication.gui.LapViewer;
import munichmotorsport.photocellapplication.gui.RaceTable;
import timber.log.Timber;

/**
 * Created by jonas on 01.04.16.
 */
public class LapClickListener implements TableDataClickListener<String[]>  {

    Context context;

    public LapClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onDataClicked(int rowIndex, String[] clickedLap){
        Timber.e(clickedLap[2]);
        Intent intent = new Intent(context, LapViewer.class);
        intent.putExtra("LapInfo", clickedLap);
        context.startActivity(intent);
    }


}
