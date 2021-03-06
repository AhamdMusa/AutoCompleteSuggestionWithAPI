package com.etl.autocompletesuggestionwithapi.retrofit;


import com.etl.autocompletesuggestionwithapi.PlaceAPI.Predictions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("place/autocomplete/json")
    Call<Predictions> getPlacesAutoComplete(
            @Query("input") String input,
            @Query("types") String types,
            @Query("location") String location,
            @Query("radius") String radius,
            @Query("key") String key
    );
}
