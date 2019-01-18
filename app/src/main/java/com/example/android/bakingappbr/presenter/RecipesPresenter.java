package com.example.android.bakingappbr.presenter;


import android.util.Log;
import android.view.View;

import com.example.android.bakingappbr.R;
import com.example.android.bakingappbr.model.RecipesItem;
import com.example.android.bakingappbr.netData.RecipesItemService;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RecipesPresenter implements RecipesContract.Presenter{
    private final String TAG=RecipesPresenter.class.getSimpleName();

    private RecipesContract.View mRecipesView;
    private RecipesItemService mRecipesItemService;
    private CompositeDisposable mCompositeDisposable=new CompositeDisposable();

    public interface Callbacks{
        void EachRecipeDetails(RecipesItem recipesItem);
    }


    public RecipesPresenter(RecipesContract.View mRecipesView, RecipesItemService recipesItemService) {
        this.mRecipesView= mRecipesView;
        this.mRecipesItemService=recipesItemService;

    }
    @Override
    public void getRecipes() {
        Observable<ArrayList<RecipesItem>> retrofitObserver;
        retrofitObserver = this.mRecipesItemService.netRequestRecipes();
        retrofitObserver.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(netRecipeObserver());
    }

    private Observer<ArrayList<RecipesItem>> netRecipeObserver() {
        return new Observer<ArrayList<RecipesItem>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mCompositeDisposable.add(d);
            }

            @Override
            public void onNext(ArrayList<RecipesItem> netRecipeResult) {
                Log.d(TAG, "onNext mainmenu");
                ArrayList<RecipesItem> recipeList = new ArrayList<>();
                recipeList.addAll(netRecipeResult);
                if(mRecipesView.isActive()) {
                    mRecipesView.refreshUI(recipeList);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "mainmenu onError");
                if(mRecipesView.isActive()) {
                    mRecipesView.showError("mainmenu onError");
                }
            }

            @Override
            public void onComplete() {
                Log.d(TAG, " mainmenu onComplete");
            }
        };
    }




    @Override
    public void recipeChosen(RecipesItem recipes, View view) {
        ((Callbacks) view.getContext()).EachRecipeDetails(recipes);
    }

    @Override
    public void viewDestroy() {
        if ( mCompositeDisposable!= null) {
        mCompositeDisposable.clear();

        mCompositeDisposable = null;
        }

    }
}

