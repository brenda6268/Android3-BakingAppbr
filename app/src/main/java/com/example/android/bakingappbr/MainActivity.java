package com.example.android.bakingappbr;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingappbr.model.RecipesItem;
import com.example.android.bakingappbr.presenter.RecipesPresenter;





public class MainActivity extends AppCompatActivity implements RecipesPresenter.Callbacks{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new RecipesFragment().newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, "RecipesFragment")
                    .commit();
        }
    }


    @Override
    public void EachRecipeDetails(RecipesItem recipes) {
        Intent intent = EachRecipeActivity.ERIntent(this, recipes);
        IngredientWidgetService.startActionWidgets(this, recipes);


        startActivity(intent);
    }
 }

