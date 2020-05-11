package com.example.kshitijjaju.group02_hw02;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    TextView tvThrdPwdCount,tvThrdPwdLen,tvAsyncPwdCount,tvAsyncPwdLen;
    SeekBar sbThrdPwdCount,sbThrdPwdLen,sbAsyncPwdCount,sbAsyncPwdLen;
    Button btnGenerate;
    int tCount,tLength, aCount,aLength, counter=0;
    ProgressBar progressBar;
    Handler handler;
    LinearLayout linlyt_progress;
    ArrayList<String> pwdAsyncList,pwdThreadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Password Generator");

        tvThrdPwdCount =(TextView) findViewById(R.id.tvThrdPwdCount);
        tvThrdPwdLen =(TextView) findViewById(R.id.tvThrdPwdLen);
        tvAsyncPwdCount =(TextView) findViewById(R.id.tvAsyncPwdCount);
        tvAsyncPwdLen =(TextView) findViewById(R.id.tvAsyncPwdLen);
        sbThrdPwdCount = (SeekBar) findViewById(R.id.sbThrdPwdCount);
        sbThrdPwdLen = (SeekBar) findViewById(R.id.sbThrdPwdLen);
        sbAsyncPwdCount = (SeekBar) findViewById(R.id.sbAsyncPwdCount);
        sbAsyncPwdLen = (SeekBar) findViewById(R.id.sbAsyncPwdLen);
        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        linlyt_progress = (LinearLayout) findViewById(R.id.linlyt_progress);
        Log.d("SDK::",""+Build.VERSION.SDK_INT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sbThrdPwdCount.setMin(1);
            sbThrdPwdLen.setMin(7);
            sbAsyncPwdCount.setMin(1);
            sbAsyncPwdLen.setMin(7);
        }
        sbThrdPwdCount.setMax(10);
        sbThrdPwdLen.setMax(23);
        sbAsyncPwdCount.setMax(10);
        sbAsyncPwdLen.setMax(23);

        tvThrdPwdCount.setText(""+1);
        tvAsyncPwdCount.setText(""+1);
        tvAsyncPwdLen.setText(""+7);
        tvThrdPwdLen.setText(""+7);


        pwdAsyncList = new ArrayList<String>();
        pwdThreadList = new ArrayList<String>();



        sbAsyncPwdCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAsyncPwdCount.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbAsyncPwdLen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAsyncPwdLen.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbThrdPwdCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvThrdPwdCount.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbThrdPwdLen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvThrdPwdLen.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(null!=msg && null!=msg.getData().getString("PasswordGenerated") && !msg.getData().getString("PasswordGenerated").equals("") ){

                    String passwords= msg.getData().getString("PasswordGenerated");
                    Log.d("handleMessage:::::::::",""+passwords);
                    counter++;
                    pwdThreadList.add(passwords);
                    Log.d("Counter_thread::::",""+counter);
                    progressBar.setProgress(counter);
                    Log.d("counter_sum:::",""+(aCount+tCount));
                    if(counter>1 && counter==(aCount+tCount)){
                        Log.d("inside::::","if   condition");
                        Intent intent = new Intent(MainActivity.this, GeneratedPasswords.class);
                        intent.putStringArrayListExtra("pwdAsyncList", pwdAsyncList);
                        intent.putStringArrayListExtra("pwdThreadList", pwdThreadList);
                        startActivity(intent);
                        finish();
                    }

                    return true;
                }
                return false;
            }
        });

        final ExecutorService executorService = Executors.newFixedThreadPool(2);



        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                aCount = Integer.parseInt(tvAsyncPwdCount.getText().toString());
                aLength = Integer.parseInt(tvAsyncPwdLen.getText().toString());

                for(int i = 0; i < aCount; i++) {
                    new DoAsyncTask().execute(aLength);
                }


                tCount = Integer.parseInt(tvThrdPwdCount.getText().toString());
                tLength = Integer.parseInt(tvThrdPwdLen.getText().toString());

                for(int i=0; i < tCount; i++) {
                    executorService.execute(new DoThreadTask(tLength));
                }

                progressBar.setMax(aCount+tCount);
                linlyt_progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });



    }

    class DoAsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //progressBar.setVisibility(View.VISIBLE);
            Log.d("onPostExecute:::::",""+s);
            Log.d("counter_sum:::",""+(aCount+tCount));
            if(counter>1 && counter==(aCount+tCount)){
                Log.d("inside::::","if   condition");
                Intent intent = new Intent(MainActivity.this, GeneratedPasswords.class);
                intent.putStringArrayListExtra("pwdAsyncList", pwdAsyncList);
                intent.putStringArrayListExtra("pwdThreadList", pwdThreadList);
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("Counter_async::::",""+values[0]);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String password="";
            password= Util.getPassword(integers[0]);
            counter++;
            publishProgress(counter);
            pwdAsyncList.add(password);
            return password;
        }
    }

    class DoThreadTask implements Runnable{

        int pwdLen;
        DoThreadTask(int pwdLen){
            this.pwdLen = pwdLen;
        }


        @Override
        public void run() {
            String password="";
            password=Util.getPassword(pwdLen);

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("PasswordGenerated",password);
            message.setData(bundle);
            handler.sendMessage(message);

        }
    }
}
