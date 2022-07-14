package com.example.samplecleanarchtectureandroid.network;

import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NetworkAPIServices {
    @GET(URL)
    Observable<JsonElement> getProducts();

    String URL = "products";
}
