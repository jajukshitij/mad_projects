package com.example.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayNote extends AppCompatActivity {

    TextView tv_note;
    Button btn_close;
    SharedPreferences sharedPreferences;
    String msgId,prefToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_note);
        setTitle("Note Details");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        prefToken = sharedPreferences.getString("token"," ");

        tv_note = findViewById(R.id.tv_note);
        btn_close = findViewById(R.id.btn_close);

        if(null!=getIntent()){
            msgId = getIntent().getStringExtra("msgId");
            //prefToken = getIntent().getStringExtra("token");
        }

        Log.d("msgId"," "+msgId);
        Log.d("prefToken"," "+prefToken);
        OkHttpClient client = new OkHttpClient();

        Request request = null;
        try {
            request = new Request.Builder()
                    .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/get?id="+ URLEncoder.encode(msgId, "UTF-8"))
                    .header("x-access-token",prefToken)
                    .addHeader("Content-Type","application/x-www-form-urlencoded")
                    //.post(formBody)
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
                    JSONObject noteObject = jsonObject.getJSONObject("note");
                    final String noteString = noteObject.getString("text");
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tv_note.setText(noteString);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
