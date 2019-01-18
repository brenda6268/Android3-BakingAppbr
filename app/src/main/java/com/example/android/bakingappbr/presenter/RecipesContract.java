package com.example.android.bakingappbr.presenter;




import com.example.android.bakingappbr.model.RecipesItem;

import java.util.ArrayList;

public interface RecipesContract {

    interface Presenter extends BasePresenter {

        void getRecipes();

    }

    interface View extends BaseView<Presenter> {

        void refreshUI(ArrayList<RecipesItem> recipeList);

        void showError(String string);

    }





}
