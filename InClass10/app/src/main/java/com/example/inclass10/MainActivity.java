package com.example.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login,btn_signup;
    String token;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=et_email.getText() && null!= et_password.getText()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("email",et_email.getText().toString());
                    editor.putString("password",et_password.getText().toString());
                    editor.commit();
                    signIn(et_email.getText().toString(), et_password.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_fields), Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String prefToken = sharedPreferences.getString("token",null);
        Log.d("token_main_onstart:::::::::", " "+prefToken);
        /*String email = sharedPreferences.getString("email",null);
        String password = sharedPreferences.getString("password",null);*/
        if(null!=prefToken){
            Intent intent = new Intent(MainActivity.this, NotesList.class);
            //intent.putExtra("token",token);
            startActivity(intent);
            finish();
        }
    }

    public void signIn (String email, String password) {

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .build();


        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login")
                .header("Content-Type","application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RESPONSE_Async:::","onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d("RESPONSE_Async:::",responseString);

                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    boolean auth = jsonObject.getBoolean("auth");
                    if(auth) {
                        token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("token",token);
                        editor.apply();
                        Log.d("token_main:::::::::", " "+token);
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this, NotesList.class);
                        //intent.putExtra("token",token);
                        startActivity(intent);
                        finish();
                    }

                    if(!auth){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.auth_fail), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

}
