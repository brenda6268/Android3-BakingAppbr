package com.example.android.bakingappbr.presenter;


import com.example.android.bakingappbr.model.RecipesItem;

public interface BasePresenter {


    void recipeChosen(RecipesItem recipesItem, android.view.View view);
    void viewDestroy();
}
