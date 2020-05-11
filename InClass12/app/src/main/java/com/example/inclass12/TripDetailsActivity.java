package com.example.inclass12;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TripDetailsActivity extends AppCompatActivity {

    Button btn_map;
    RecyclerView rv_results;
    ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    PlacesAdapter placesAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("cityDataArrayList");
    String location,keyword;
    CityData cityData = new CityData();
    ArrayList<PlacesData> placesDataArrayList = new ArrayList<>();
    TextView tv_no_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        setTitle("Places to Visit");

        btn_map = findViewById(R.id.btn_map);
        rv_results = findViewById(R.id.rv_results);
        tv_no_list = findViewById(R.id.tv_no_list);

            Bundle bundle = getIntent().getExtras();
            if(null!=bundle) {
                cityData = (CityData) bundle.getSerializable("cityData");
                location = cityData.latitude+","+cityData.longitude;
                keyword = cityData.keyword;

                if (arrayListSizeCheck(cityData.placesDataArrayList)) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(TripDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_results.setLayoutManager(layoutManager);
                    placesAdapter = new PlacesAdapter(TripDetailsActivity.this, cityData.placesDataArrayList);
                    rv_results.setAdapter(placesAdapter);
                }
            }

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cityData111::"," "+cityData);
                Intent intent = new Intent(TripDetailsActivity.this,MapActivity.class);
                Bundle bundle= new Bundle();
                bundle.putSerializable("cityData",cityData);
                bundle.putSerializable("placesDataArrayList",cityData.placesDataArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    public boolean arrayListSizeCheck(ArrayList<PlacesData> placesDataArrayList){

        if(null!=placesDataArrayList && placesDataArrayList.size()>0) {

            tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            btn_map.setEnabled(true);
            return true;
        }else {
            tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            btn_map.setEnabled(false);
            return false;
        }
    }

}
