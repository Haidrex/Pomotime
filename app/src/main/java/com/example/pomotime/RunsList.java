package com.example.pomotime;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class RunsList extends AppCompatActivity {

    private ListView runsList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runslist);

        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        runsList = (ListView) findViewById(R.id.runsview);
        loadData();
    }

    public void loadData() {
        DBHelper dbHelper = new DBHelper(RunsList.this);
        List<Runs> allItems = dbHelper.getAllRuns();
        RunsListAdapter adapter = new RunsListAdapter(this, allItems);
        runsList.setAdapter(adapter);
    }
}
