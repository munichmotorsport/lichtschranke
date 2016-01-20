package com.example.jonas.firststeps.gui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jonas.firststeps.R;
import com.example.jonas.firststeps.model.LSData;
import com.example.jonas.firststeps.provider.DataProvider;
import com.example.jonas.firststeps.provider.FakeDataProviderImpl;
import com.example.jonas.firststeps.util.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class LiveActivity extends AppCompatActivity {

    private DataProvider provider;
    private AsyncTask<Void, LSData, Void> asyncTask;
    private AsyncTask<Void, LSData, Void> publishAdapter;

    ArrayList<String> stringArray = new ArrayList<>();
    ArrayAdapter<String> adapter;
    final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);
    private boolean isRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        setTitle("Live View");
        stringArray = MainActivity.getData(this);
        final ListView lv_view2 = (ListView) findViewById(R.id.lv_list2);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        lv_view2.setAdapter(adapter);
        provider = new FakeDataProviderImpl();
    }

    public void clickInterface(View v) {
        Timber.d("Clicked IF");
        TextView tv = (TextView) v;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            asyncTask = null;
            tv.setText("IF");
        } else {
            tv.setText("Cancel");
            asyncTask = createAsyncTask();
            asyncTask.execute();
        }
    }

    private AsyncTask<Void, LSData, Void> createAsyncTask() {
        return new AsyncTask<Void, LSData, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    if (isCancelled()) {
                        return null;
                    }
                    List<LSData> newData = provider.getNewData();
                    publishProgress(newData.toArray(new LSData[newData.size()]));
                    ThreadUtils.sleep(2000);
                }
            }




            @Override
            protected void onProgressUpdate(LSData... newData) {
                for (LSData data : newData) {
//                    stringArray.add(data.toString());
                    adapter.add(data.toString());;
                }
            }
        };
    }



    final Runnable DataInRates = new Runnable() {
        public void run() {
           //showData();
            System.out.println("Data");
        }
    };

    public void showData(){
        stringArray = MainActivity.getData(this);
        Timber.d(stringArray.toString());
        ArrayAdapter<String> Scheduledadapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.lv_list2);
        listView.setAdapter(Scheduledadapter);
    }

        private ScheduledFuture future;

        public void clickContentProvider(View v) {
        Timber.d("Clicked CP");
    }

    public void clickBroadcast(View v) {
        Timber.d("Clicked BC");

        if(!isRunning) {
            future = scheduled.scheduleAtFixedRate(DataInRates, 0, 5, TimeUnit.SECONDS);
            isRunning = true;
        }
        else{
            future.cancel(true);
            isRunning = false;
        }
    }


}
