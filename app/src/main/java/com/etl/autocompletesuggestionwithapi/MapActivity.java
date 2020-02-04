package com.etl.autocompletesuggestionwithapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView locationTV;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private double latitude;
    private double longitude;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        locationTV=findViewById(R.id.textView);

        Bundle b = getIntent().getExtras();
        latitude = b.getDouble("Longitute");
        longitude = b.getDouble("Latitute");
         latLng=new LatLng(longitude,latitude);
      //  Toast.makeText(this, String.valueOf(latitude+" "+longitude), Toast.LENGTH_SHORT).show();
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map,mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
        //getDevicrLocation();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapBackgroundChange(mMap);

        mMap.addMarker(new MarkerOptions().position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            List<Address> addresses= new Geocoder(MapActivity.this).getFromLocation(longitude,latitude,1);
            String myLocation=addresses.get(0).getFeatureName();
            locationTV.setText(myLocation);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void mapBackgroundChange(GoogleMap mMap) {

        try {
            boolean success=mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.changemapdesignapi));
            if (!success){
                Log.e("MainActivity","Style parch faild");
            }

        }
        catch (Resources.NotFoundException e){
            Log.e("MainActivity","can't fing style",e);
        }


    }
}

