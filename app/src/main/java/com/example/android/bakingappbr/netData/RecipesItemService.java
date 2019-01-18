package com.example.android.bakingappbr.netData;



import com.example.android.bakingappbr.model.RecipesItem;
import java.util.ArrayList;
import io.reactivex.Observable;


public class RecipesItemService {
    private DataInterface mDataInterface;

    public RecipesItemService(){

        mDataInterface = RetrofitFactory.getRetrofitRecipes().create(DataInterface.class);;
    }

    public Observable<ArrayList<RecipesItem>> netRequestRecipes() {
        Observable observer = mDataInterface.getRecipesItem();
        return observer;
    }

}
