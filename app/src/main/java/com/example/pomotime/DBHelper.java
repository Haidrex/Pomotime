package com.example.pomotime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CATEGORY_TABLE = "CATEGORY_TABLE";
    public static final String COLUMN_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String TODO_TABLE = "TODO_TABLE";
    public static final String COLUMN_TODO_TITLE = "TODO_TITLE";
    public static final String COLUMN_TODO_CATEGORY = "TODO_CATEGORY";
    public static final String COLUMN_CATEGORY_ID = "ID";
    public static final String COLUMN_TODO_ID = "ID";

    public DBHelper(@Nullable Context context) {
        super(context, "pomo.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS " + CATEGORY_TABLE + "(" + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CATEGORY_NAME + " TEXT)";
        String createTableStatement2 = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + " (" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TODO_TITLE + " TEXT, " + COLUMN_TODO_CATEGORY + " TEXT);";
        db.execSQL(createTableStatement);
        db.execSQL(createTableStatement2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String createTableStatement2 = "CREATE TABLE IF NOT EXISTS " + TODO_TABLE + " (" + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TODO_TITLE + " TEXT, " + COLUMN_TODO_CATEGORY + " TEXT);";
        db.execSQL(createTableStatement2);
    }

    public boolean insertCategory(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CATEGORY_NAME, category.getName());

        long insert = db.insert(CATEGORY_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else {

            return true;
        }
    }

    public List<String> getAllCategories(){
        List<String> categories = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + CATEGORY_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                categories.add(cursor.getString(1));
            } while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return categories;
    }

    public boolean insertTodo(ListItem todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TODO_TITLE, todo.getTitle());
        cv.put(COLUMN_TODO_CATEGORY, todo.getCategory());

        long insert = db.insert(TODO_TABLE, null, cv);
        if(insert == -1){
            return false;
        }
        else {

            return true;
        }
    }

    public List<ListItem> getAllTodos(){

        List<ListItem> listitems = new ArrayList<ListItem>();

        String queryString = "SELECT * FROM " + TODO_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            do{
                int todoId = cursor.getInt(0);
                String todoTitle = cursor.getString(1);
                String todoCategory = cursor.getString(2);
                ListItem newitem = new ListItem(todoId, todoTitle, todoCategory);
                listitems.add(newitem);
            }while(cursor.moveToNext());
        }
        else{

        }

        cursor.close();
        db.close();
        return listitems;
    }

    public boolean deleteTodo(ListItem todoItem){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TODO_TABLE + " WHERE " + COLUMN_TODO_ID + " = " + todoItem.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }
}
