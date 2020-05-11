package com.example.inclass12;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements CityAdapter.OnInteractionListener{

    EditText et_city;
    Button btn_search_city;
    RecyclerView rv_results;
    ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    CityAdapter cityAdapter;
    TextView tv_no_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("City for Trip");

        et_city = findViewById(R.id.et_city);
        btn_search_city = findViewById(R.id.btn_search_city);
        rv_results = findViewById(R.id.rv_results);
        tv_no_list = findViewById(R.id.tv_no_list);


        btn_search_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchCity(et_city.getText().toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void searchCity(String cityName) throws UnsupportedEncodingException {
        OkHttpClient client = new OkHttpClient();

        String url ="https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input="+
                URLEncoder.encode(cityName, "UTF-8")+
                "&inputtype=textquery&fields=formatted_address,geometry&key=AIzaSyCy8MfZpuwRmC2OoxGRc-fvxzPe46Zn0EA&type=city_hall";

        Log.d("RESPONSE_Async","url::"+url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("RESPONSE_Async:::","onFailure");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d("RESPONSE_Async:::",responseString);

                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray candidatesArray = jsonObject.getJSONArray("candidates");

                    for(int i=0 ; i< candidatesArray.length();i++){
                        CityData cityData = new CityData();
                        JSONObject cityObject = candidatesArray.getJSONObject(i);
                        cityData.address = cityObject.getString("formatted_address");
                        JSONObject geoObject =cityObject.getJSONObject("geometry");
                        JSONObject locObject =geoObject.getJSONObject("location");
                        cityData.latitude = Float.parseFloat(locObject.getString("lat"));
                        cityData.longitude = Float.parseFloat(locObject.getString("lng"));

                        cityDataArrayList.add(cityData);
                    }

                    Log.d("RESPONSE_Async","cityDataArrayList:::"+cityDataArrayList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (arrayListSizeCheck(cityDataArrayList)) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
                                rv_results.setLayoutManager(layoutManager);
                                cityAdapter = new CityAdapter(MainActivity.this, cityDataArrayList);
                                rv_results.setAdapter(cityAdapter);
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public boolean arrayListSizeCheck(ArrayList<CityData> cityDataArrayList){

        if(null!=cityDataArrayList && cityDataArrayList.size()>0) {

            tv_no_list.setVisibility(View.INVISIBLE);
            rv_results.setVisibility(View.VISIBLE);
            return true;
        }else {
            tv_no_list.setVisibility(View.VISIBLE);
            rv_results.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
