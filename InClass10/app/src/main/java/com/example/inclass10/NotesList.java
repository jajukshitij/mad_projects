package com.example.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotesList extends AppCompatActivity implements  NotesAdapter.OnAdapterInteractionListener{

    TextView tv_username;
    ImageButton btn_logout;
    RecyclerView rv_results;
    Button btn_addNote;
    SharedPreferences sharedPreferences;
    ArrayList<NotesData> notesDataArrayList = new ArrayList<>();
    NotesAdapter notesAdapter;
    String prefToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        setTitle("Notes");

        tv_username = findViewById(R.id.tv_username);
        btn_logout = findViewById(R.id.btn_logout);
        rv_results = findViewById(R.id.rv_results);
        btn_addNote = findViewById(R.id.btn_addNote);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefToken = sharedPreferences.getString("token"," ");

        Log.d("token_Notes1:::::::::", " "+prefToken);
        /*if(null!= getIntent()){
            prefToken = getIntent().getStringExtra("token");
            Log.d("token_Notes2:::::::::", " "+prefToken);
        }*/

        getUserDetails();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/logout")
                        .build();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token",null);
                editor.putString("email",null);
                editor.putString("password",null);
                editor.apply();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("RESPONSE_Async:::","onFailure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("RESPONSE_Async:::",response.body().string());
                    }
                });

                Intent intent = new Intent(NotesList.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });


        btn_addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotesList.this, AddNote.class);
                intent.putExtra("token",prefToken);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        fetchNotesList();
    }

    public void getUserDetails(){

        OkHttpClient client = new OkHttpClient();
        Request request = null;
        try {
            request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me")
                    .header("x-access-token",prefToken)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RESPONSE_Async:::","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d("RESPONSE_Async:::",responseString);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseString);
                    final String name = jsonObject.getString("name");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_username.setText(name);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void fetchNotesList(){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall")
                .header("x-access-token",prefToken)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RESPONSE_Async_NotesList:::","onFailure");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("token", null);
                editor.apply();
                Intent intent = new Intent(NotesList.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d("RESPONSE_Async_NotesList:::", " " +response.code());
                notesDataArrayList.clear();

                try {

                    JSONObject jsonObject = new JSONObject(responseString);

                    if (200 != response.code()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token", null);
                        editor.apply();
                        Intent intent = new Intent(NotesList.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        JSONArray notesArray = jsonObject.getJSONArray("notes");

                        for (int i = 0; i < notesArray.length(); i++) {

                            NotesData notesData = new NotesData();
                            JSONObject notesObject = notesArray.getJSONObject(i);

                            notesData.id = notesObject.getString("_id");
                            notesData.userId = notesObject.getString("userId");
                            notesData.text = notesObject.getString("text");
                            notesData.v = notesObject.getString("__v");
                            notesDataArrayList.add(notesData);
                        }

                        Log.d("notesDataArrayList:::", notesDataArrayList.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (arrayListSizeCheck(notesDataArrayList)) {
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(NotesList.this, LinearLayoutManager.VERTICAL, false);
                                    rv_results.setLayoutManager(layoutManager);
                                    notesAdapter = new NotesAdapter(NotesList.this, prefToken, notesDataArrayList);
                                    rv_results.setAdapter(notesAdapter);
                                }
                            }
                        });
                    }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                }

        });
    }

    public boolean arrayListSizeCheck(ArrayList<NotesData> chatDataArrayList){

        if(null!=chatDataArrayList && chatDataArrayList.size()>0) {

            //tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            //tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }


    @Override
    public void notifyDataset() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                fetchNotesList();
                //notesAdapter.notifyDataSetChanged();
            }
        });

    }
}
