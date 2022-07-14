package com.example.samplecleanarchtectureandroid.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.samplecleanarchtectureandroid.network.NetworkModule;

public class BaseActivity extends AppCompatActivity {
    private Deps mDeps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDeps = DaggerDeps.builder().networkModule(new NetworkModule()).build();
        getDeps().inject(this);
    }

    public Deps getDeps() {
        return mDeps;
    }

}
