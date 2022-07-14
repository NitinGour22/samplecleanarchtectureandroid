package com.example.samplecleanarchtectureandroid.network;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.samplecleanarchtectureandroid.base.ViewModelFactory;
import com.example.samplecleanarchtectureandroid.util.LogUtil;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

@Module
public class NetworkModule {
    private final String BASE_URL = "https://fakestoreapi.com/";

    private static final String TAG = NetworkModule.class.getSimpleName();
    private static final String contentType = "application/json";
    private static final String accept = "application/json";

    @Provides
    @Singleton
    @Inject
    @Named("provideRetrofit2")
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.setLenient().create();
    }

    @Provides
    @Singleton
    @Inject
    @Named("provideRetrofit2")
    Retrofit provideRetrofit(@Named("provideRetrofit2") Gson gson, @Named("provideRetrofit2") OkHttpClient okHttpClient) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        builder.client(okHttpClient);
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        return builder.build();
    }

    @Provides
    @Singleton
    NetworkAPIServices getApiCallInterface(@Named("provideRetrofit2") Retrofit retrofit) {
        return retrofit.create(NetworkAPIServices.class);
    }

    @Provides
    @Singleton
    @Named("provideRetrofit2")
    OkHttpClient getRequestHeader() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (LogUtil.isEnableLogs) { //dont show logs from here
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(interceptor);
        }
        try {
            httpClient.addInterceptor(new Interceptor() {

                        @NonNull
                        @EverythingIsNonNull
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = null;
                            Response response = null;
                            String url = original.url().toString();

                            if (original.method().equalsIgnoreCase("POST")
                                    || original.method().equalsIgnoreCase("PUT")) {//if request method is post or put then encrypt body

                                //get requested body
                                RequestBody oldBody = original.body();
                                Buffer buffer = new Buffer();
                                oldBody.writeTo(buffer);
                                String strBody = buffer.readUtf8();

                                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                                RequestBody newEncryptedBody = RequestBody.create(mediaType, strBody);

                                Request.Builder requestBuilder = original.newBuilder();
                                requestBuilder.method(original.method(), newEncryptedBody);
                                request = requestBuilder.build();
                                response = chain.proceed(request);

                                String strResponse = new String(response.body().bytes(), "UTF-8");

                                LogUtil.printLog(TAG, "HEADER Content-Type => " + contentType);
                                LogUtil.printLog(TAG, "URL => " + url);
                                LogUtil.printLog(TAG, "REQUEST => " + strBody);
                                LogUtil.printLog(TAG, "RESPONSE => " + strResponse.trim());

                                MediaType contentType = response.body().contentType();
                                ResponseBody bodyRes = ResponseBody.create(contentType, strResponse);
                                return response.newBuilder().body(bodyRes).build();
                            } else {
                                //GET request
                                Request.Builder requestBuilder = original.newBuilder();
                                requestBuilder.method(original.method(), original.body());
                                request = requestBuilder.build();
                                response = chain.proceed(request);

                                String strResponse = new String(response.body().bytes(), "UTF-8");

                                LogUtil.printLog(TAG, "HEADER Content-Type => " + contentType);
                                LogUtil.printLog(TAG, "URL => " + url);
                                LogUtil.printLog(TAG, "RESPONSE => " + strResponse.trim());

                                MediaType contentType = response.body().contentType();
                                ResponseBody bodyRes = ResponseBody.create(contentType, strResponse);
                                return response.newBuilder().body(bodyRes).build();
                            }

                        }
                    })
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpClient.build();


    }

    @Provides
    @Singleton
    Service getRepository(NetworkAPIServices networkAPIServices) {
        return new Service(networkAPIServices);
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory getViewModelFactory(Service myService) {
        return new ViewModelFactory(myService);
    }
}
