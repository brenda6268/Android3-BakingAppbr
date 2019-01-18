package com.example.android.bakingappbr.presenter;


import com.example.android.bakingappbr.model.RecipeStep;


import java.util.ArrayList;

public interface EachRecipeContract {

    interface Presenter extends BasePresenter {

        void getEachRecipe(ArrayList<RecipeStep> stepList, int currentStep,
                           String recipeName, android.view.View view);

    }

    interface View extends BaseView<EachRecipeContract.Presenter> {
    }



}
