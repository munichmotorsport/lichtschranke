package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.content.Intent;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import munichmotorsport.photocellapplication.gui.RaceTable;
import munichmotorsport.photocellapplication.gui.RaceViewer;


/**
 * Created by jonas on 01.04.16.
 */
public class RaceClickListener implements TableDataClickListener<String[]> {

    Context context;

    public RaceClickListener(Context context){
        this.context = context;
    }

    @Override
    public void onDataClicked(int rowIndex, String[] clickedRace){
        Intent intent = new Intent(context, RaceTable.class);

        intent.putExtra("RaceInfo", clickedRace);
        context.startActivity(intent);
    }




}