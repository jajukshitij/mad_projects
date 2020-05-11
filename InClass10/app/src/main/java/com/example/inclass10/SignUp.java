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

public class SignUp extends AppCompatActivity {

    EditText et_fname, et_lname, et_s_email, et_s_password, et_confpwd;
    Button btn_s_signup, btn_cancel;
    String token;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        et_fname = findViewById(R.id.et_fname);
        et_lname = findViewById(R.id.et_lname);
        et_s_email = findViewById(R.id.et_s_email);
        et_s_password = findViewById(R.id.et_s_password);
        et_confpwd = findViewById(R.id.et_confpwd);
        btn_s_signup = findViewById(R.id.btn_s_signup);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_s_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(null!= et_fname.getText() && null!= et_lname.getText() && null!= et_s_email.getText() && null!= et_s_password.getText() && null!= et_confpwd.getText() && et_s_password.getText().toString().equals(et_confpwd.getText().toString())) {
                    createAccount(et_fname.getText().toString(),et_s_email.getText().toString(), et_s_password.getText().toString());
                }else{
                    Toast.makeText(SignUp.this, getResources().getString(R.string.enter_correct),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void createAccount (String name, String email, String password){


        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("email",email)
                .add("password",password)
                .build();


        Request request = new Request.Builder()
                .url("http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register")
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
                        editor.commit();
                        Intent intent = new Intent(SignUp.this, NotesList.class);
                        //intent.putExtra("token",token);
                        startActivity(intent);
                        finish();
                    }

                    if(!auth){
                        Toast.makeText(SignUp.this, getResources().getString(R.string.signup_fail), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
