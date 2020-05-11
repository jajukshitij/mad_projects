package com.example.inclass12;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<PlacesData> placesDataArrayList = new ArrayList<>();
    CityData cityData = new CityData();
    LatLngBounds.Builder builder;
    CameraUpdate cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("Places on Map");

        Bundle bundle = getIntent().getExtras();
        if(null!=bundle) {
            cityData = (CityData) bundle.getSerializable("cityData");
            placesDataArrayList = (ArrayList<PlacesData>) bundle.getSerializable("placesDataArrayList");
            Log.d("cityData222::"," "+cityData);
            Log.d("placesDataArrayList11::"," "+placesDataArrayList);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        builder = new LatLngBounds.Builder();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                final LatLng centerPoint = new LatLng(cityData.latitude, cityData.longitude);
                mMap.addMarker(new MarkerOptions().position(centerPoint).title("City Center"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(centerPoint));

                for(int i=0;i < placesDataArrayList.size();i++) {
                    PlacesData placesData = new PlacesData();
                    placesData = placesDataArrayList.get(i);
                    LatLng markerPoint = new LatLng(placesData.latitude, placesData.longitude);
                    mMap.addMarker(new MarkerOptions().position(markerPoint).title(placesData.placetitle));
                    builder.include(new LatLng(placesData.latitude,placesData.longitude));
                }
                builder.build();
                int padding = 200;
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                LatLngBounds bounds = builder.build();
                cu = CameraUpdateFactory.newLatLngBounds(bounds,width,height,padding);
                mMap.animateCamera(cu);

            }
        });

    }



}
