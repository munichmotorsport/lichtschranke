package com.example.jonas.firststeps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class NextActivity extends AppCompatActivity {

    ArrayList<String> stringArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        stringArray = this.getIntent().getStringArrayListExtra("MainActivity.Data");
        final ListView lv_view2 = (ListView) findViewById(R.id.lv_list2);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        lv_view2.setAdapter(adapter);
    }
}
