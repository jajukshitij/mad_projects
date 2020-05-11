package com.example.kshitijjaju.a801075892_midterm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class MainActivity extends AppCompatActivity {

    Button button_search;
    EditText et_keyword;
    TextView tv_limit;
    SeekBar sb_limit;
    RadioGroup rg_sort;
    RadioButton rb_track, rb_artist;
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
        et_keyword = findViewById(R.id.et_keyword);
        tv_limit = findViewById(R.id.tv_limit);
        sb_limit =  findViewById(R.id.sb_limit);
        rv_results = findViewById(R.id.rv_results);
        rg_sort = findViewById(R.id.rg_sort);
        rb_track = findViewById(R.id.rb_track);
        rb_artist = findViewById(R.id.rb_artist);
        linlyt_progress = findViewById(R.id.linlyt_progress);
        linlyt_progress.setVisibility(View.GONE);

        sb_limit.incrementProgressBy(1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sb_limit.setMin(5);
        }
        sb_limit.setMax(30);

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
                    url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=00b7bc688cb57e624260fbf914373ae8&q="
                            + URLEncoder.encode(keyword, "UTF-8") + "&page_size=" + URLEncoder.encode(limit, "UTF-8");
                    if(rb_track.isChecked()){
                        url = url +"&s_track_rating=desc";
                    }else {
                        url = url +"&s_artist_rating=desc";
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                if(!keyword.equals("") && isConnected()) {
                    Log.d("url::::",url);
                    musicDataArrayList.clear();
                    linlyt_progress.setVisibility(View.VISIBLE);
                    rv_results.setVisibility(View.INVISIBLE);
                    new GetMusicAsync().execute(url);
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                }
            }
        });




        rg_sort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String url = null, keyword, limit;
                keyword = et_keyword.getText().toString();
                limit = tv_limit.getText().toString();

                try {
                    url = "https://api.musixmatch.com/ws/1.1/track.search?apikey=00b7bc688cb57e624260fbf914373ae8&q="
                            + URLEncoder.encode(keyword, "UTF-8") + "&page_size=" + URLEncoder.encode(limit, "UTF-8");

                    if (R.id.rb_artist == checkedId) {
                        url = url + "&s_artist_rating=desc";
                    } else {
                        url = url+ "&s_track_rating=desc";

                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                if(!keyword.equals("") && isConnected()) {
                    Log.d("url::::",url);
                    musicDataArrayList.clear();
                    linlyt_progress.setVisibility(View.VISIBLE);
                    rv_results.setVisibility(View.INVISIBLE);
                    new GetMusicAsync().execute(url);
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_keyword), Toast.LENGTH_SHORT).show();
                }

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

    private class GetMusicAsync extends AsyncTask<String,Void,ArrayList<MusicData>> {

        @Override
        protected void onPostExecute(ArrayList<MusicData> musicData) {
            linlyt_progress.setVisibility(View.GONE);
            mAdapter = new MusicAdapter(MainActivity.this,musicData);
            rv_results.setAdapter(mAdapter);
            rv_results.setVisibility(View.VISIBLE);
            Log.d("onPostExecute_music::::",""+ musicData);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            linlyt_progress.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
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
                    JSONObject jsonMessage = jsonObject.getJSONObject("message");
                    JSONObject jsonBody = jsonMessage.getJSONObject("body");
                    JSONArray trackListArray = jsonBody.getJSONArray("track_list");
                    for (int i = 0; i < trackListArray.length(); i++) {

                        MusicData musicData = new MusicData();
                        JSONObject trackObject = trackListArray.getJSONObject(i);
                        JSONObject musicObject = trackObject.getJSONObject("track");
                        musicData.trackName = musicObject.getString("track_name");
                        musicData.artistName = musicObject.getString("artist_name");
                        musicData.albumName = musicObject.getString("album_name");
                        musicData.trackShareUrl = musicObject.getString("track_share_url");

                        try {
                            SimpleDateFormat sd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                            musicData.updatedTime = sd1.parse(musicObject.getString("updated_time"));
                        }
                        catch(Exception e) {
                        }
                        musicDataArrayList.add(musicData);
                        publishProgress();
                    }
                    Log.d("doInBackground_musi::::",""+ musicDataArrayList);
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

