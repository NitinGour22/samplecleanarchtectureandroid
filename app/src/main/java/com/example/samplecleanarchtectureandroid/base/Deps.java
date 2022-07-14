package com.example.samplecleanarchtectureandroid.base;

import com.example.samplecleanarchtectureandroid.view.MainActivity;
import com.example.samplecleanarchtectureandroid.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {NetworkModule.class})
@Singleton
public interface Deps {
    void inject(BaseActivity baseActivity);

    void inject(MainActivity mainActivity);
}
