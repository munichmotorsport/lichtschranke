package com.example.jonas.firststeps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> stringArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Listenspaß als Test");

        final Button btn_clearList = (Button) findViewById(R.id.btn_clearList);
        final Button btn_next = (Button) findViewById(R.id.btn_next);

        btn_clearList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clearList();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goTo_nextActivity();
            }
        });
    }

    public void saveData(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        stringArray.add(message);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void deleteData(View view) {
        EditText editText = (EditText) findViewById(R.id.deleteText);
        String message = editText.getText().toString();
        stringArray.remove(message);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void clearList() {
        stringArray.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringArray);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void goTo_nextActivity() {
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("MainActivity.Data", stringArray);
        startActivity(intent);
    }
}
