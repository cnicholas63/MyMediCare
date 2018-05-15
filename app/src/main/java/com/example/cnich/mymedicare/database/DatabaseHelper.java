package com.example.cnich.mymedicare.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.cnich.mymedicare.AppConstants;


/**
 * Created by Chris on 10/05/2016.
 * Database helper class, provides methods for accessing the user database
 * Adapted from http://www.vogella.com/tutorials/AndroidSQLite/article.html
 */
public class DatabaseHelper extends SQLiteOpenHelper implements AppConstants {

    private static final String DATABASE_CREATE = // The SQLite string used to create the table
        "create table " + TABLE_ENTRIES + " (" +
            COLUMN_ID + " integer primary key autoincrement, " +
            USERNAME + " text, " + PASSWORD + " text, " +
            NAME + " text, " + AGE + " text, " +
            ADDRESS1 + " text, " + POSTCODE + " text, " +
            DOCTOR + " text " + ");";

    // Constructor for the class
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); // Call super constructor with database info
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE); // Execute the DATABASE_CREATE sql string if database does not exist
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // not needed in this app - (at this stage)
    }
}
