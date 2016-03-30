package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import db.Lap;
import de.codecrafters.tableview.TableDataAdapter;
import timber.log.Timber;

/**
 * Created by jonas on 30.03.16.
 */
public class LapTableDataAdapter extends TableDataAdapter<String[]>{

    private static final String LOG_TAG = LapTableDataAdapter.class.getName();

    private int paddingLeft = 20;
    private int paddingTop = 15;
    private int paddingRight = 20;
    private int paddingBottom = 15;
    private int textSize = 18;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;
    private int fastestLapColour = 0xffff00ff;
    private String[][] data;
    private long fastestLap = 100000;

    public LapTableDataAdapter(final Context context, final String[][] data) {
        super(context, data);
        this.data = data;

        for(String[] s:data){
        if(Long.parseLong(s[2]) < fastestLap){
            fastestLap = Long.parseLong(s[2]);
            }
        }
        Timber.e("fastest lap: %s", fastestLap);
    }

    @Override
    public View getCellView(final int rowIndex, final int columnIndex, final ViewGroup parentView){
        final TextView textView = new TextView(getContext());

        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTypeface(textView.getTypeface(), typeface);
        textView.setTextSize(textSize);


        if (columnIndex == 2 && Long.parseLong(data[rowIndex][columnIndex]) == fastestLap) {
            textView.setTextColor(fastestLapColour);
        }
        else {
            textView.setTextColor(textColor);
        }

        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        try {
            final String textToShow = getItem(rowIndex)[columnIndex];
            textView.setText(textToShow);
        } catch(final IndexOutOfBoundsException e) {
            Log.w(LOG_TAG, "No Sting given for row " + rowIndex + ", column " + columnIndex + ". "
                    + "Caught exception: " + e.toString());
            // Show no text
        }

        return textView;
    }
}
