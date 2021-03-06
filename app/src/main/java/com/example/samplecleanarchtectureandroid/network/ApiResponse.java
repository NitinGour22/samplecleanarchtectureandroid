package com.example.samplecleanarchtectureandroid.network;

import static com.example.samplecleanarchtectureandroid.network.Status.ERROR;
import static com.example.samplecleanarchtectureandroid.network.Status.LOADING;
import static com.example.samplecleanarchtectureandroid.network.Status.SUCCESS;

import com.google.gson.JsonElement;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/*this class is responsible for network response*/
public class ApiResponse {
    public final Status status;

    @Nullable
    public final JsonElement data;

    @Nullable
    public final Throwable error;

    private ApiResponse(Status status, @Nullable JsonElement data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull JsonElement data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }

}
