package com.example.inclass12;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreateTripActivity extends AppCompatActivity implements  TripsAdapter.OnInteractionListener{

    String[] keywordArray;
    Spinner sp_keyword;
    TextView tv_city, tv_no_list;
    Context context;
    Button btn_create_trip;
    RecyclerView rv_results;
    ArrayList<CityData> cityDataArrayList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("cityDataArrayList");
    TripsAdapter tripsAdapter;
    CityData cityData = new CityData();
    int limit =15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);
        context = CreateTripActivity.this;
        setTitle("Create Trip");

        tv_city = findViewById(R.id.tv_city);
        sp_keyword = findViewById(R.id.sp_keyword);
        btn_create_trip = findViewById(R.id.btn_create_trip);
        rv_results = findViewById(R.id.rv_results);
        tv_no_list = findViewById(R.id.tv_no_list);

        Bundle bundle = getIntent().getExtras();
        if( null!=bundle){
            cityData = (CityData) bundle.getSerializable("cityData");
            tv_city.setText(cityData.address);
            Log.d("cityData1::"," "+cityData);
        }

        keywordArray = new String[]{"airport", "aquarium", "laundry", "bar", "cafe", "park", "casino", "parking"};
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, keywordArray);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_keyword.setAdapter(dataAdapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cityDataArrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    cityDataArrayList.add( dataSnapshot1.getValue(CityData.class));
                }
                if (arrayListSizeCheck(cityDataArrayList)) {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CreateTripActivity.this, LinearLayoutManager.VERTICAL, false);
                    rv_results.setLayoutManager(layoutManager);
                    tripsAdapter = new TripsAdapter(CreateTripActivity.this, cityDataArrayList);
                    rv_results.setAdapter(tripsAdapter);
                }
                Log.d("firebase:", "cityDataArrayList1::"+cityDataArrayList);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebase:", "Failed to read value.1", error.toException());
            }
        });

        btn_create_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    searchPlaces(cityData.latitude+","+cityData.longitude,String.valueOf(sp_keyword.getSelectedItem()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void searchPlaces(String location,final String keyword) throws UnsupportedEncodingException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+
                        URLEncoder.encode(location, "UTF-8")+"&radius=24140&type="+
                        URLEncoder.encode(keyword, "UTF-8")+"&key=AIzaSyCy8MfZpuwRmC2OoxGRc-fvxzPe46Zn0EA")
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
                    JSONArray resultsArray = jsonObject.getJSONArray("results");

                    Date today = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                    String date = formatter.format(today);
                    cityData.date = date;

                    cityData.tripName = cityData.address +" "+ keyword;
                    cityData.placesDataArrayList = new ArrayList<>();

                    if(resultsArray.length()<15){
                        limit = resultsArray.length();
                    }

                    for(int i=0 ; i< limit ;i++){
                        PlacesData placesData = new PlacesData();
                        JSONObject cityObject = resultsArray.getJSONObject(i);
                        placesData.placetitle = cityObject.getString("name");
                        JSONObject geoObject =cityObject.getJSONObject("geometry");
                        JSONObject locObject =geoObject.getJSONObject("location");
                        placesData.latitude = Float.parseFloat(locObject.getString("lat"));
                        placesData.longitude = Float.parseFloat(locObject.getString("lng"));
                        cityData.placesDataArrayList.add(placesData);

                        cityDataArrayList.add(cityData);
                    }

                    Log.d("RESPONSE_Async","cityDataArrayList:::"+cityDataArrayList);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cityData.keyword = keyword ;
                            Log.d("cityData2::"," "+cityData);
                            String keyName = myRef.push().getKey();
                            cityData.setFirebaseKey(keyName);
                            myRef.child(keyName).setValue(cityData);

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    cityDataArrayList.clear();
                                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                                    {
                                        cityDataArrayList.add( dataSnapshot1.getValue(CityData.class));
                                    }
                                    tripsAdapter.notifyDataSetChanged();
                                    Log.d("firebase:", "cityDataArrayList2::"+cityDataArrayList);
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    // Failed to read value
                                    Log.w("firebase:", "Failed to read value.2", error.toException());
                                }
                            });
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
