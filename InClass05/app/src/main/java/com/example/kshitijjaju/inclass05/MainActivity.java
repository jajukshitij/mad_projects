package com.example.kshitijjaju.inclass05;

/*In Class Assignment 5
* Kshitij Jaju 801075892
* Smruthi Rajgopala 801073413*/

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetImageAsync.IData {


    Button go;
    ImageButton previous, next;
    ImageView imageView;
    TextView tvkeyword,tvLoading;
    ProgressBar progressBar;
    ArrayList<String> imageUrlLists = new ArrayList<String>();
    int indexUrl=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = findViewById(R.id.btn_go);
        previous = (ImageButton) findViewById(R.id.ibtn_prev);
        next = findViewById(R.id.ibtn_next);
        tvkeyword = findViewById(R.id.tv_keyword);
        imageView = findViewById(R.id.iv_image);
        imageView.setVisibility(View.VISIBLE);
        progressBar = findViewById(R.id.progress_circular);
        progressBar.setVisibility(View.GONE);
        tvLoading = findViewById(R.id.tv_loading);
        tvLoading.setVisibility(View.GONE);
        previous.setEnabled(false);
        next.setEnabled(false);


        if(isConnected()) {
            new GetKeywordsAsync().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
        } else{
            Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
        }


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexUrl++;
                if(indexUrl< imageUrlLists.size()){
                    if(isConnected()) {
                        new GetImageAsync(MainActivity.this).execute(imageUrlLists.get(indexUrl));
                    }else{
                        Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                }else if(indexUrl== imageUrlLists.size()){
                    indexUrl=0;
                    if(isConnected()) {
                        new GetImageAsync(MainActivity.this).execute(imageUrlLists.get(indexUrl));
                    }else{
                        Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                }
                imageView.setVisibility(View.GONE);
                tvLoading.setText(getString(R.string.loading_next));
                tvLoading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexUrl--;
                if(indexUrl>=0){
                    if(isConnected()) {
                        new GetImageAsync(MainActivity.this).execute(imageUrlLists.get(indexUrl));
                    }else{
                        Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                } else if(indexUrl<0){
                    indexUrl= imageUrlLists.size()-1;
                    if(isConnected()) {
                        new GetImageAsync(MainActivity.this).execute(imageUrlLists.get(indexUrl));
                    }else{
                        Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                    }
                }

                imageView.setVisibility(View.GONE);
                tvLoading.setText(getString(R.string.loading_previous));
                tvLoading.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

            }
        });

    }


    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class GetKeywordsAsync extends AsyncTask<String, Void, String[]> {

        String[] keywordArray;
    @Override
    protected void onPostExecute(String[] strings) {

        tvLoading.setText(getString(R.string.loading));

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.choose_keyword))
                        .setItems(keywordArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tvLoading.setText(getString(R.string.loading));

                                String keyWord= keywordArray[which];
                                tvkeyword.setText(keyWord);

                                if(isConnected()) {
                                    new GetUrlAsync(keyWord).execute("http://dev.theappsdr.com/apis/photos/index.php");
                                }else{
                                    Toast.makeText(MainActivity.this,getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.create().show();
            }
        });

    }


    @Override
    protected String[] doInBackground(String... strings) {


        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;
        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);

                }
                result= stringBuilder.toString();
                keywordArray = result.split(";");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close open connections and reader
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return keywordArray;
    }
}


    private class GetUrlAsync extends AsyncTask<String, Void, ArrayList<String>> {

        String keyword;
        public GetUrlAsync(String keyword) {
            this.keyword = keyword;
        }

        @Override
        protected void onPreExecute() {
            imageView.setVisibility(View.GONE);
            tvLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<String> imageUrlLists) {


            if(null!=imageUrlLists && imageUrlLists.size()>0){
                if(imageUrlLists.size()==1){
                    previous.setEnabled(false);
                    next.setEnabled(false);
                }else {
                    previous.setEnabled(true);
                    next.setEnabled(true);
                }
                    new GetImageAsync(MainActivity.this).execute(imageUrlLists.get(0));
            } else{
                previous.setEnabled(false);
                next.setEnabled(false);
                imageView.setVisibility(View.VISIBLE);
                tvLoading.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                imageView.setImageResource(0);
                Toast.makeText(MainActivity.this,getString(R.string.no_image),Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            imageUrlLists.clear();

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {

                String strUrl = strings[0] + "?" + "keyword=" + URLEncoder.encode(keyword, "UTF-8");
                URL url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        imageUrlLists.add(line);
                    }
                    /*Log.d("result Array1::::",""+String.valueOf(imageUrlLists.get(0)));
                    Log.d("result Array2::::",""+String.valueOf(imageUrlLists.get(1)));
                    Log.d("result Array3::::",""+String.valueOf(imageUrlLists.get(2)));*/
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //Close open connections and reader
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return imageUrlLists;
        }
    }

    @Override
    public void handledata(Bitmap bitmap) {
        Log.d("Bitmap3",""+bitmap);
        if(bitmap!=null) {
            if(imageUrlLists.size()==1){
                previous.setEnabled(false);
                next.setEnabled(false);
            }else {
                previous.setEnabled(true);
                next.setEnabled(true);
            }
            progressBar.setVisibility(View.GONE);
            tvLoading.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(bitmap);

        }else{
            imageView.setImageResource(0);
            tvLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,getString(R.string.no_image),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateprogress() {

        //handle the loading and loading next inside button click
        imageView.setVisibility(View.GONE);
        previous.setEnabled(false);
        next.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        tvLoading.setVisibility(View.VISIBLE);

    }

}
