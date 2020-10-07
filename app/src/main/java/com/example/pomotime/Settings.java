package com.example.pomotime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Settings extends AppCompatActivity {

    private Context context = this;
    private EditText editWork;
    private Button workButton;
    private EditText editBreak;
    private Button breakButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        editWork = (EditText) findViewById(R.id.editWork);
        editBreak = (EditText) findViewById(R.id.editBreak);
        workButton = (Button) findViewById(R.id.buttonWork);
        breakButton = (Button) findViewById(R.id.buttonBreak);

        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTime = editWork.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("newWorkTime", newTime);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(context, Settings.class);
                intent.putExtra("flag", true);
                context.startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
