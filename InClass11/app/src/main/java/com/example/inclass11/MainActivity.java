package com.example.inclass11;

import android.graphics.Color;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    ArrayList<LatLongPoint> locationList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String json = loadJSONFromAsset();

        Gson gson = new Gson();
        Location location = gson.fromJson(json,Location.class);
        locationList =  location.getLocationList();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.

        LatLng startPoint = new LatLng(35.344255,-80.73303166666666);
        LatLng endPoint = new LatLng(35.354335,-80.74401833333333);
        mMap.addMarker(new MarkerOptions().position(startPoint).title(getResources().getString(R.string.start_point)));
        mMap.addMarker(new MarkerOptions().position(endPoint).title(getResources().getString(R.string.end_point)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPoint));

        final LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i=1;i<locationList.size();i++){

            LatLongPoint prev = locationList.get(i-1);
            LatLongPoint current = locationList.get(i);

            builder.include(new LatLng(current.latitude,current.longitude));

            mMap.addPolyline(new PolylineOptions()
                    .add( new LatLng(prev.latitude,prev.longitude),new LatLng(current.latitude,current.longitude))
                    .width(8)
                    .color(Color.RED)
            );
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLngBounds bounds = builder.build();
        /*mMap.setLatLngBoundsForCameraTarget(bounds);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,0));*/
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                mMap.animateCamera(cameraUpdate);

            }
        });


    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("trip.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
