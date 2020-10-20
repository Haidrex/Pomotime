package com.example.pomotime;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class TodoList extends AppCompatActivity {
    private Context context = this;
    private ListView myTodoList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myTodoList = (ListView) findViewById(R.id.todoview);
        myTodoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder builder =new AlertDialog.Builder(TodoList.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are you sure you want to delete " + (position + 1));
                final AdapterView<?> parent = a;
                final int positionToRemove = position;
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ListItem clickedItem = (ListItem) parent.getItemAtPosition(positionToRemove);
                        DBHelper dbHelper = new DBHelper(TodoList.this);
                        dbHelper.deleteTodo(clickedItem);
                        loadData();
                    }});
                builder.show();
            }
        });

        loadData();
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

    public void loadData(){
        DBHelper dbHelper = new DBHelper(TodoList.this);
        List<ListItem> allItems = dbHelper.getAllTodos();
        ListAdapter adapter = new ListAdapter(this, allItems);
        myTodoList.setAdapter(adapter);
    }
}
