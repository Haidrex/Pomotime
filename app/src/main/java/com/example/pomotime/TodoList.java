package com.example.pomotime;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
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
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class TodoList extends AppCompatActivity implements FilterDialog.FilterDialogListener{
    private Context context = this;
    private ListView myTodoList;
    private Button filterButton;
    private Spinner chooseCategory;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String WORKING = "working";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myTodoList = (ListView) findViewById(R.id.todoview);
        filterButton = (Button) findViewById(R.id.filter_button);
        registerForContextMenu(myTodoList);



        myTodoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder builder =new AlertDialog.Builder(TodoList.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are you sure you want to delete item");
                final AdapterView<?> parent = a;
                final int positionToRemove = position;
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ListItem clickedItem = (ListItem) parent.getItemAtPosition(positionToRemove);
                        DBHelper dbHelper = new DBHelper(TodoList.this);
                        dbHelper.deleteTodo(clickedItem);
                        if(dbHelper.isCurrentlyWorking() == false) {
                            if (clickedItem.getTitle().equals(dbHelper.getCurrentlyWorking())) {
                                dbHelper.deleteCurrentlyWorking();
                            }
                        }
                        loadData();
                    }});
                builder.show();
            }
        });
        myTodoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        loadData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.todo_menu, menu);
    }

    //TODO fix this delete statement(goes out of bound)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.delete:
                ListItem clickedItem = (ListItem) myTodoList.getItemAtPosition(info.position);
                DBHelper dbHelper = new DBHelper(TodoList.this);
                dbHelper.deleteTodo(clickedItem);
                if(dbHelper.isCurrentlyWorking() == false) {
                    if (clickedItem.getTitle().equals(dbHelper.getCurrentlyWorking())) {
                        dbHelper.deleteCurrentlyWorking();
                    }
                }
                loadData();
            case R.id.work:
                ListItem selectedItem = (ListItem) myTodoList.getItemAtPosition(info.position);
                String workOn = (String) selectedItem.getTitle();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(WORKING, workOn);
                editor.apply();
                Intent intent = new Intent(TodoList.this, MainActivity.class);
                startActivity(intent);
        }
        return true;
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

    public void openDialog(){
        FilterDialog filterDialog = new FilterDialog();
        filterDialog.show(getSupportFragmentManager(),"example dialog");
    }

    @Override
    public void applyFilter(String category) {

        DBHelper dbHelper = new DBHelper(TodoList.this);
        List<ListItem> filteredTodos = dbHelper.getFilteredTodos(category);
        ListAdapter adapter = new ListAdapter(this, filteredTodos);
        myTodoList.setAdapter(adapter);
    }
}
