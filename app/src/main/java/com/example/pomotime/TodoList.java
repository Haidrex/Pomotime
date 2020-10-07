package com.example.pomotime;

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
    private Button addItemButton;
    private EditText addItemText;
    private ListAdapter adapter;
    //private ArrayList<String> listItems;
    //private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        myTodoList = (ListView) findViewById(R.id.todoview);
        final List<ListItem> items = new ArrayList<ListItem>();
        Intent intent = getIntent();
        items.add(new ListItem(intent.getStringExtra("title"),intent.getStringExtra("category") ));
        adapter = new ListAdapter(this, items);
        myTodoList.setAdapter(adapter);

        myTodoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder builder =new AlertDialog.Builder(TodoList.this);
                builder.setTitle("Delete?");
                builder.setMessage("Are you sure you want to delete " + (position + 1));
                final int positionToRemove = position;
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                builder.show();
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
