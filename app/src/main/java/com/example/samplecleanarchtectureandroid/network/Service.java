package com.example.samplecleanarchtectureandroid.network;

import com.google.gson.JsonElement;

import io.reactivex.Observable;

public class Service {

    private NetworkAPIServices networkAPIServices;

    public Service(NetworkAPIServices networkAPIServices) {
        this.networkAPIServices = networkAPIServices;
    }

    public Observable<JsonElement> getProducts() {
        return networkAPIServices.getProducts();
    }
}
