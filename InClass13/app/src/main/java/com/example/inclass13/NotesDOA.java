package com.example.inclass13;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class NotesDOA {

    private SQLiteDatabase database;

    public NotesDOA(SQLiteDatabase database) {
        this.database = database;
    }


    public long save(NotesData notesData) {
        ContentValues values = new ContentValues();
        values.put(NotesTable.COLUMN_NOTE,notesData.getNote());
        values.put(NotesTable.COLUMN_PRIORITY,notesData.getPriority());
        values.put(NotesTable.COLUMN_STATUS,notesData.getStatus());
        values.put(NotesTable.COLUMN_TIME,notesData.getTime());
        return database.insert(NotesTable.TABLENAME,null,values);
    }

    public boolean update(NotesData notesData) {
        ContentValues values = new ContentValues();
        values.put(NotesTable.COLUMN_NOTE,notesData.getNote());
        values.put(NotesTable.COLUMN_PRIORITY,notesData.getPriority());
        values.put(NotesTable.COLUMN_STATUS,notesData.getStatus());
        values.put(NotesTable.COLUMN_TIME,notesData.getTime());
        return database.update(NotesTable.TABLENAME,values,NotesTable.COLUMN_ID+"=?",new String[]{notesData.getId()+""})>0;

    }
    public boolean delete(NotesData notesData)
    {
        Log.d("check","delete");
        return database.delete(NotesTable.TABLENAME,NotesTable.COLUMN_ID+"=?",new String[]{notesData.getId()+""})>0;

    }
    public NotesData get(long id)
    {
        NotesData notesData = null;
        Cursor c = database.query(true,NotesTable.TABLENAME,new String[]
                        {NotesTable.COLUMN_ID,NotesTable.COLUMN_NOTE,NotesTable.COLUMN_PRIORITY,NotesTable.COLUMN_STATUS,NotesTable.COLUMN_TIME},NotesTable.COLUMN_ID+"=?",new String[]{id+""},
                null,null,null,null);
        if(c!=null&& c.moveToFirst()){
            notesData = buildNoteFromCursor(c);
            if(!c.isClosed()) {
                c.close();
            }

        }
        return notesData;

    }
    public ArrayList<NotesData> getAll()
    {
        ArrayList<NotesData> notesDataArrayList = new ArrayList<NotesData>();
        Cursor c = database.query(NotesTable.TABLENAME,new String[]
                        {NotesTable.COLUMN_ID,NotesTable.COLUMN_NOTE,NotesTable.COLUMN_PRIORITY,NotesTable.COLUMN_STATUS,NotesTable.COLUMN_TIME},
                null,null,null,null,null);
        if(c!=null&& c.moveToFirst()){
            do {
                NotesData notesData = buildNoteFromCursor(c);
                notesDataArrayList.add(notesData);
            }while (c.moveToNext());
            if(!c.isClosed()) {
                c.close();
            }

        }
        return notesDataArrayList;

    }
    private NotesData buildNoteFromCursor(Cursor c){
        NotesData notesData = null;
        if(c!=null) {
            notesData = new NotesData();
            notesData.setId(c.getInt(0));
            notesData.setNote(c.getString(1));
            notesData.setPriority(c.getString(2));
            notesData.setStatus(c.getInt(3));
            notesData.setTime(c.getString(4));
        }
        return notesData;
    }

}
