package com.example.android.bakingappbr.netData;

import com.example.android.bakingappbr.model.RecipesItem;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;



public interface DataInterface {

    @GET(" ")
    Observable<ArrayList<RecipesItem>> getRecipesItem();
}
