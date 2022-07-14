package com.example.samplecleanarchtectureandroid.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.samplecleanarchtectureandroid.viewmodel.ProductListViewModel;
import com.example.samplecleanarchtectureandroid.network.Service;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private Service service;

    @Inject
    public ViewModelFactory(Service service) {
        this.service = service;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductListViewModel.class)) {
            return (T) new ProductListViewModel(service);
        } else {
            throw new IllegalArgumentException("Unknown class name");
        }
    }
}
