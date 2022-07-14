package com.example.samplecleanarchtectureandroid.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.samplecleanarchtectureandroid.network.ApiResponse;
import com.example.samplecleanarchtectureandroid.network.Service;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProductListViewModel extends ViewModel {
    private Service service;
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public MutableLiveData<ApiResponse> getProductResponse() {
        return responseLiveData;
    }

    public ProductListViewModel(Service service) {
        this.service = service;
    }

    public void getAllProducts() {
        disposables.add(service.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                .subscribe(
                        result -> responseLiveData.setValue(ApiResponse.success(result)),
                        throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                ));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
