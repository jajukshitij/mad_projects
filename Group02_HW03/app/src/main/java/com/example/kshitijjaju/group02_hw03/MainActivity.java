package com.example.kshitijjaju.group02_hw03;

/*Homework 03
* MainActivity
* Kshitij Jaju
* Smruthi Rajagopal*/

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button button_search, button_reset;
    EditText et_keyword;
    TextView tv_limit;
    SeekBar sb_limit;
    Switch switch_sort;
    RecyclerView rv_results;
    ArrayList<MusicData> musicDataArrayList = new ArrayList<>();
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    LinearLayout linlyt_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_search = findViewById(R.id.button_search);
        button_reset = findViewById(R.id.button_reset);
        et_keyword = findViewById(R.id.et_keyword);
        tv_limit = findViewById(R.id.tv_limit);
        sb_limit =  findViewById(R.id.sb_limit);
        switch_sort = findViewById(R.id.switch_price_date);
        rv_results = findViewById(R.id.rv_results);
        linlyt_progress = findViewById(R.id.linlyt_progress);
        linlyt_progress.setVisibility(View.GONE);

        sb_limit.incrementProgressBy(1);
        sb_limit.setMin(10);
        sb_limit.setMax(40);

        sb_limit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_limit.setText(Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = null, keyword, limit;
                keyword = et_keyword.getText().toString();
                limit = tv_limit.getText().toString();

                    try {
                        url = "https://itunes.apple.com/search?term=" + URLEncoder.encode(keyword, "UTF-8") + "&limit=" + URLEncoder.encode(limit, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                if(!keyword.equals("") && isConnected()) {
                        Log.d("url",url);
                    musicDataArrayList.clear();
                    linlyt_progress.setVisibility(View.VISIBLE);
                    new GetMusicAsync().execute(url);
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_keyword.setText("");
                sb_limit.setProgress(10);
            }
        });


        rv_results.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rv_results.setLayoutManager(layoutManager);

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

    class MusicComparator implements Comparator<MusicData> {
        @Override
        public int compare(MusicData o1, MusicData o2) {
            if(switch_sort.isChecked()) {
                return o1.date.compareTo(o2.date);
            }else{
                return o1.trackPrice.compareTo(o2.trackPrice);
            }
        }
    }

    private class GetMusicAsync extends AsyncTask<String,Void,ArrayList<MusicData>>{

        @Override
        protected void onPostExecute(ArrayList<MusicData> musicData) {
            linlyt_progress.setVisibility(View.GONE);
            mAdapter = new MusicAdapter(MainActivity.this,musicData);
            rv_results.setAdapter(mAdapter);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            linlyt_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<MusicData> doInBackground(String... strings) {

            URL url = null;
            HttpURLConnection connection = null;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String jsonString = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {

                        MusicData musicData = new MusicData();
                        JSONObject musicObject = resultsArray.getJSONObject(i);
                        musicData.trackName = musicObject.getString("trackName");
                        musicData.artistName = musicObject.getString("artistName");
                        musicData.albumName = musicObject.getString("collectionName");
                        musicData.albumPrice = musicObject.getString("collectionPrice");
                        musicData.trackPrice = musicObject.getDouble("trackPrice");
                        musicData.artworkUrl = musicObject.getString("artworkUrl100");
                        musicData.genre = musicObject.getString("primaryGenreName");

                        try {
                            SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            musicData.date = sd1.parse(musicObject.getString("releaseDate"));
                        }
                        catch(Exception e) {
                        }
                        musicDataArrayList.add(musicData);
                        publishProgress();
                    }

                    Collections.sort(musicDataArrayList,new MusicComparator());

                    return musicDataArrayList;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;

        }
    }


}
