package munichmotorsport.photocellapplication.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import munichmotorsport.photocellapplication.R;

public class RaceTable extends AppCompatActivity {
    long[] times = new long[100];
    String[] cars = new String[100];
    int[] laps = new int[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race_table);

        fillTable();
        }

    public void fillTable(){
        TableLayout maintable = (TableLayout)findViewById(R.id.maintable);
        TextView headerCar = (TextView) findViewById(R.id.car);
        headerCar.getLayoutParams().width = headerCar.getWidth();

        TableRow tr[]= new TableRow[40];
        TextView tv[] = new TextView[3];

        long time = 23000L;
        int lap = 1;
        String car = "PWe7.16";

        for (int n = 0; n < 40; n++) {
            times[n] = time;
            time++;
            cars[n] = car;
            laps[n] = lap;
            lap++;
            tr[n] = new TableRow(this);
            tr[n].setId(n);


            for (int j = 0; j < 3; j++) {
                tv[j] = new TextView(this);
                tv[j].setId(j);
                if(j == 0) {
                    tv[j].setText(cars[n]);
                }
                if(j == 1) {
                    tv[j].setText(Integer.toString(laps[n]));
                }
                if(j == 2) {
                    tv[j].setText(Long.toString(times[n]));
                }
                tr[n].addView(tv[j]);
            }
            maintable.addView(tr[n]);
        }
    }
    }
