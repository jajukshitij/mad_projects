package com.example.inclass13;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DataBaseDataManager {

    private Context mContext;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private NotesDOA notesDOA;

    public DataBaseDataManager(Context mContext, DatabaseHelper databaseHelper, SQLiteDatabase database, NotesDOA notesDOA) {
        this.mContext = mContext;
        this.databaseHelper = databaseHelper;
        this.database = database;
        this.notesDOA = notesDOA;
    }

    public void close(){
        if(database!=null){
            database.close();
        }
    }
    public NotesDOA getNotesDOA(){
        return this.notesDOA;
    }
    public long saveTask(NotesData notesData){
        return this.notesDOA.save(notesData);
    }
    public boolean deleteTask(NotesData notesData){
        return this.notesDOA.delete(notesData);
    }
    public boolean UpdateTask(NotesData notesData){
        return this.notesDOA.update(notesData);
    }
    public NotesData getTask(long id){
        return this.notesDOA.get(id);
    }
    public ArrayList<NotesData> getAllTask(){
        return this.notesDOA.getAll();
    }
}
