package com.example.kshitijjaju.group02_hw02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GeneratedPasswords extends AppCompatActivity {

    LinearLayout linlyt_thread,linlyt_async;
    ArrayList<String> pwdAsyncList, pwdThreadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_passwords);
        setTitle("Generated Passwords");

        linlyt_thread = findViewById(R.id.linlyt_thread);
        linlyt_async = findViewById(R.id.linlyt_async);
        pwdAsyncList = new ArrayList<String>();
        pwdThreadList = new ArrayList<String>();




        if (getIntent() != null && getIntent().getExtras() != null) {
            pwdAsyncList = getIntent().getStringArrayListExtra("pwdAsyncList");
            pwdThreadList = getIntent().getStringArrayListExtra("pwdThreadList");

            LayoutInflater inflaterThread = LayoutInflater.from(this);
            Log.d("pwdAsyncList",""+pwdAsyncList);
            linlyt_thread.removeAllViews();

            for (int i = 0; i < pwdThreadList.size(); i++) {

                View viewThread = inflaterThread.inflate(R.layout.item, linlyt_thread,false);
                TextView tvPwdThread = viewThread.findViewById(R.id.tvPwdThread);

                tvPwdThread.setText(pwdThreadList.get(i));

                linlyt_thread.addView(viewThread);
            }

            LayoutInflater inflaterAsync = LayoutInflater.from(this);
            Log.d("pwdThreadList",""+pwdThreadList);
            linlyt_async.removeAllViews();

            for (int i = 0; i < pwdAsyncList.size(); i++) {

                View viewAsync = inflaterAsync.inflate(R.layout.item, linlyt_async, false);
                TextView tvPwdAsync = viewAsync.findViewById(R.id.tvPwdThread);

                tvPwdAsync.setText(pwdAsyncList.get(i));

                linlyt_async.addView(viewAsync);
            }

        }





    }
}
