package com.example.pomotime;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TodoList extends AppCompatActivity {

    private ListView myTodoList;
    private Button addItemButton;
    private EditText addItemText;

    private ArrayList<String> listItems;
    private ArrayAdapter<String> itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        myTodoList = (ListView) findViewById(R.id.todoview);
        addItemButton = (Button) findViewById(R.id.addItemButton);
        addItemText = (EditText) findViewById(R.id.addNewItem);
        listItems = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems);
        myTodoList.setAdapter(itemsAdapter);

        myTodoList.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listItems.remove(position);
                itemsAdapter.notifyDataSetChanged();
                return false;
            }
        }));
    }

    public void addItem(View v){
        String item = addItemText.getText().toString();
        itemsAdapter.add(item);
        addItemText.setText("");
        itemsAdapter.notifyDataSetChanged();
    }
}
