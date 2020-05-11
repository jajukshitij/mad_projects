package com.example.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddNote extends AppCompatActivity {

    EditText et_new_note;
    Button btn_add;
    SharedPreferences sharedPreferences;
    String prefToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Add Note");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefToken = sharedPreferences.getString("token"," ");
        Log.d("token_Add1:::::::::", " "+prefToken);

        et_new_note = findViewById(R.id.et_new_note);
        btn_add = findViewById(R.id.btn_add);
        /*if(null!= getIntent()){
            prefToken = getIntent().getStringExtra("token");
            Log.d("token_Add2:::::::::", " "+prefToken);
        }*/


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("text", et_new_note.getText().toString())
                        .build();

                Request request = new Request.Builder()
                        .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post")
                        .header("x-access-token",prefToken)
                        .addHeader("Content-Type","application/x-www-form-urlencoded")
                        .post(formBody)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("RESPONSE_Async:::","onFailure");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d("RESPONSE_Async:::",response.body().string());
                        finish();
                    }
                });

            }
        });
    }
}
