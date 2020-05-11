package com.example.inclass13;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class NotesTable {
    static final String TABLENAME = "notes";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_NOTE = "note";
    static final String COLUMN_PRIORITY = "priority";
    static final String COLUMN_STATUS = "status";
    static final String COLUMN_TIME = "time";


    static public void onCreate (SQLiteDatabase database){
        StringBuilder stringBuilder =new StringBuilder();
        stringBuilder.append("CREATE TABLE"+ TABLENAME + " (");
        stringBuilder.append(COLUMN_ID +" INTEGER PRIMARY KEY," );
        stringBuilder.append(COLUMN_NOTE +" TEXT, ");
        stringBuilder.append(COLUMN_PRIORITY +" TEXT, ");
        stringBuilder.append(COLUMN_STATUS +" INT, ");
        stringBuilder.append(COLUMN_TIME +" TEXT, ");
        Log.d("onCreate query ::::",""+stringBuilder.toString());
        try{
            database.execSQL(stringBuilder.toString());
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase database,int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        NotesTable.onCreate(database);
    }

}
