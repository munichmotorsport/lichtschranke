package munichmotorsport.photocellapplication.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.codecrafters.tableview.TableDataAdapter;
import timber.log.Timber;

/**
 * Created by jonas on 30.03.16.
 */
public class LapTableDataAdapter extends TableDataAdapter<String[]> {

    private static final String LOG_TAG = LapTableDataAdapter.class.getName();

    private int paddingLeft = 20;
    private int paddingTop = 15;
    private int paddingRight = 20;
    private int paddingBottom = 15;
    private int textSize = 18;
    private int typeface = Typeface.NORMAL;
    private int textColor = 0x99000000;
    private int red = 0xffff00ff;
    private int green = 0xff00ff00;
    private String[][] data;
    private long fastestLap = 0;
    private Map<String, Long> fastestLaps = new HashMap<String, Long>();

    public LapTableDataAdapter(final Context context, final String[][] data) {
        super(context, data);
        this.data = data;


        for (String[] s : data) {
            if (!fastestLaps.containsKey(s[0])) {
                fastestLaps.put(s[0], Long.parseLong(s[2]));
            } else {
                if (Long.parseLong(s[2]) < fastestLaps.get(s[0])) {
                    fastestLaps.put(s[0], Long.parseLong(s[2]));
                }
            }
            if (fastestLap == 0 || fastestLap > Long.parseLong(s[2])) {
                fastestLap = Long.parseLong(s[2]);
            }
        }


        Timber.e(fastestLaps.toString());
        Timber.e("Fastest Lap: %s", fastestLap);



    }

    /**
     * colorize the cell
     * @param rowIndex
     * @param columnIndex
     * @param parentView
     * @return
     */
    @Override
    public View getCellView(final int rowIndex, final int columnIndex, final ViewGroup parentView) {
        final TextView textView = new TextView(getContext());

        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        textView.setTypeface(textView.getTypeface(), typeface);
        textView.setTextSize(textSize);


        if (columnIndex == 2 && fastestLaps.containsKey(data[rowIndex][0]) && Long.parseLong(data[rowIndex][columnIndex]) == fastestLaps.get(data[rowIndex][0])) {
            if (Long.parseLong(data[rowIndex][columnIndex]) == fastestLap) {
                textView.setTextColor(green);
            }
            else {
                textView.setTextColor(red);
            }
        } else {
            textView.setTextColor(textColor);
        }
        

        textView.setSingleLine();
        textView.setEllipsize(TextUtils.TruncateAt.END);

        try {
            final String textToShow = getItem(rowIndex)[columnIndex];
            textView.setText(textToShow);
            if(columnIndex == 2) {
                Double time = Float.parseFloat(textView.getText().toString()) / 1000.0;
                textView.setText(time+" s");
            }
        } catch (final IndexOutOfBoundsException e) {
            Log.w(LOG_TAG, "No String given for row " + rowIndex + ", column " + columnIndex + ". "
                    + "Caught exception: " + e.toString());
            // Show no text
        }

        return textView;
    }
}
