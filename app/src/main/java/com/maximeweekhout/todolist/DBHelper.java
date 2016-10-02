package com.maximeweekhout.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maxime Weekhout on 02-10-16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "firstdb.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_TODOLIST = "todolist";
    private static final String KEY_ID = "todoid";
    private static final String KEY_NAME = "todoitem";
    private static final String KEY_CHECKED = "todochecked";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODOLIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_CHECKED + " BOOL"
                + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);

        // Create tables again
        onCreate(db);
    }

    // Create
    public void add(String item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item);
        values.put(KEY_CHECKED, false);

        // Inserting Row
        db.insert(TABLE_TODOLIST, null, values);
        db.close(); // Closing database connection
    }

    // Read
    public List<ListItem> read() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TODOLIST, null);

        List<ListItem> list = new ArrayList<ListItem>();

        if (cursor .moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                System.out.println(cursor.getColumnIndex(KEY_ID));
                ListItem item = new ListItem(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                        cursor.getInt(cursor.getColumnIndex(KEY_CHECKED))
                );
                list.add(item);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    // Update
    public void update(int id, boolean checked) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(KEY_CHECKED, checked);

        db.update(TABLE_TODOLIST, cv, KEY_ID + " = " + id, null);
        db.close();
    }

    // remove
    public void remove(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODOLIST, KEY_ID + " = '" + id + "'", null);
        db.close();
    }
}
