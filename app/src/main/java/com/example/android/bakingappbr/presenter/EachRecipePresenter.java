package com.example.android.bakingappbr.presenter;


import android.view.View;
import com.example.android.bakingappbr.model.RecipeStep;
import com.example.android.bakingappbr.model.RecipesItem;
import java.util.ArrayList;


public class EachRecipePresenter implements EachRecipeContract.Presenter{


    private EachRecipeContract.View mEachRecipeView;


    public interface Callbacks{

        void EachStep(ArrayList<RecipeStep> recipeStepList, int currentStep, String recipeName);
    }

    public EachRecipePresenter(EachRecipeContract.View view) {
        this.mEachRecipeView = view;
    }

    @Override
    public void getEachRecipe(ArrayList<RecipeStep> stepList, int currentStep, String recipeName, View view) {
        ((Callbacks) view.getContext()).EachStep(stepList, currentStep, recipeName);
    }

    @Override
    public void recipeChosen(RecipesItem recipesItem, android.view.View view){}
    @Override
    public  void viewDestroy(){}
//
}
