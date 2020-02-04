package com.etl.autocompletesuggestionwithapi;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.etl.autocompletesuggestionwithapi.PlaceAPI.Prediction;
import com.etl.autocompletesuggestionwithapi.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements PredictionInterface{


    private PlacesAutoCompleteAdapter placesAutoCompleteAdapter;
    private ActivityMainBinding binding;
    private List<Prediction> predictions;
    private Geocoder geocoder;
    public LinearLayout linearLayout;
    private double latitute;
    private double longitute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        linearLayout = findViewById(R.id.selectFromMap);

        initRecyclerView();
        hideSearchViewIcon();
        searchViewAction();
    }

    private void searchViewAction() {

        binding.searchlocationSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    predictions.clear();
                    placesAutoCompleteAdapter.notifyDataSetChanged();
                }
                placesAutoCompleteAdapter.getFilter().filter(newText);
                return true;
            }
        });

    }

    private void hideSearchViewIcon() {
        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) binding.searchlocationSV.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        magImage.setVisibility(View.GONE);
    }

    private void initRecyclerView() {
        predictions = new ArrayList<>();
        placesAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext(), predictions, this);
        binding.locationNameRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.locationNameRecyclerView.setAdapter(placesAutoCompleteAdapter);

    }

    @Override
    public void getPrediction(Prediction prediction) {
        geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(prediction.getDescription(), 1);
            hideSoftKeyboard();
            if (addresses.size()>0){
                latitute= addresses.get(0).getLatitude();
                longitute=addresses.get(0).getLongitude();

                //Toast.makeText(this, String.valueOf(addresses.get(0).getLatitude()+" "+addresses.get(0).getLongitude()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Latitute", latitute);
                b.putDouble("Longitute", longitute);
                intent.putExtras(b);
                startActivity(intent);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hideSoftKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }

    public void openMap(View view) {

        Toast.makeText(this, "Map opened", Toast.LENGTH_SHORT).show();
    }
}

