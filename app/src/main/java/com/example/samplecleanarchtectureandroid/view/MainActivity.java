package com.example.samplecleanarchtectureandroid.view;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.example.samplecleanarchtectureandroid.model.Product;
import com.example.samplecleanarchtectureandroid.util.LogUtil;
import com.example.samplecleanarchtectureandroid.viewmodel.ProductListViewModel;
import com.example.samplecleanarchtectureandroid.base.BaseActivity;
import com.example.samplecleanarchtectureandroid.base.ViewModelFactory;
import com.example.samplecleanarchtectureandroid.databinding.ActivityMainBinding;
import com.example.samplecleanarchtectureandroid.network.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends BaseActivity {
    ActivityMainBinding binding;

    @Inject
    ViewModelFactory mViewModelFactory;
    private ProductListViewModel productListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getDeps().inject(this);
        injectAPI();
        productListViewModel.getAllProducts();
    }

    private void injectAPI() {
        productListViewModel = new ViewModelProvider(this, mViewModelFactory).get(ProductListViewModel.class);
        productListViewModel.getProductResponse().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse apiResponse) {
                customResponse(apiResponse, "product");
            }
        });
    }

    private void customResponse(ApiResponse apiResponse, String isTag) {
        switch (apiResponse.status) {
            case LOADING:
                //showProgressLoader(getString(R.string.SCR_MESSAGE_JUST_GIVE_FEW_SECOND));
                break;
            case SUCCESS:
                //dismissLoader();
                if (!apiResponse.data.isJsonNull()) {
                    try {
                        if (isTag.equalsIgnoreCase("product")) {
                            Type userListType = new TypeToken<List<Product>>() {}.getType();
                            List<Product> productListResponse = new Gson().fromJson(apiResponse.data.toString(), userListType);
                            if (productListResponse != null) {
                                for (Product product : productListResponse) {
                                    LogUtil.printLog("product", product.toString());
                                }
                            } else {
                                LogUtil.printLog("product", "No Products");

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ERROR:
                //dismissLoader();
                //errorMessage(apiResponse.error.getMessage());
                break;
            default:
                break;
        }
    }

}