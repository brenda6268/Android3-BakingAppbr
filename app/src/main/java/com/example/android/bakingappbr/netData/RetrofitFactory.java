package com.example.android.bakingappbr.netData;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitFactory {

    private static Retrofit retrofitRecipes = null;
    public static final String URL_RECIPES = "http://go.udacity.com/android-baking-app-json/";

    public static Retrofit getRetrofitRecipes() {
        if (retrofitRecipes==null) {
            retrofitRecipes = new Retrofit.Builder()
                    .baseUrl(URL_RECIPES)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofitRecipes;
    }
}
