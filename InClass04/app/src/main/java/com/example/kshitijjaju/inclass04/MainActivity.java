package com.example.kshitijjaju.inclass04;
/*
InClass Assignment04
Kshitij Jaju 801075892
Sai Smruthi Rajagopal 801073413

* */
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;
    Button buttonAsync, buttonThread;
    String AsyncImageUrl,ThreadImageUrl;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Display Image");

        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        buttonAsync = (Button) findViewById(R.id.buttonAsync);
        buttonThread = (Button) findViewById(R.id.buttonThread);
        AsyncImageUrl="https://cdn.pixabay.com/photo/2014/12/16/22/25/youth-570881_960_720.jpg";
        ThreadImageUrl ="https://cdn.pixabay.com/photo/2017/12/31/06/16/boats-3051610_960_720.jpg";

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(null!= msg && null!=msg.getData().getParcelable("ImageFromThread")){

                    Bitmap myBitmap = msg.getData().getParcelable("ImageFromThread");
                    imageView.setImageBitmap(myBitmap);
                    progressBar.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    return true;

                }else if (null!= msg && msg.getData().getInt("Progress")>=0) {

                    Log.d("progressThread:::",""+ msg.getData().getInt("Progress"));

                    if (msg.getData().getInt("Progress") == 99) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                    } else if (msg.getData().getInt("Progress")<99){
                        progressBar.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.GONE);
                    }
                }
                    return false;
            }
        });

        final ExecutorService executorService = Executors.newFixedThreadPool(2);


        buttonAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                new DoAsyncTask().execute(AsyncImageUrl);

            }
        });

        buttonThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                executorService.execute(new DoThreadTask(ThreadImageUrl));

            }
        });

    }

    class DoAsyncTask extends AsyncTask<String,Integer,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Bitmap myBitmap) {
            /*String[] imageURL = null;
            imageURL[0] = AsyncImageUrl;*/
            //Bitmap myBitmap = getImageBitmap(AsyncImageUrl);
            imageView.setImageBitmap(myBitmap);

            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("progress:::",""+values[0]);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {

            for(int i = 0; i<10000; i++){
                for(int j=0; j<100; j++){

                }
                publishProgress(i);
            }
            Bitmap myBitmap = getImageBitmap(strings);
            return myBitmap;
        }


        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                Log.d("demo", url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class DoThreadTask implements Runnable {

        String imgUrl;
        DoThreadTask(String url){

            this.imgUrl = url;

        }

        @Override
        public void run() {

            Bitmap myBitmap = getImageBitmap(imgUrl);

            for(int i=0; i<10000 ; i++){

                for(int j=0; j<100; j++){

                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("Progress",i);
                message.setData(bundle);
                handler.sendMessage(message);
            }

            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putParcelable("ImageFromThread",myBitmap);
            message.setData(bundle);
            handler.sendMessage(message);

        }

        Bitmap getImageBitmap(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
