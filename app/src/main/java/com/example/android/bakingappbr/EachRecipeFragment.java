package com.example.android.bakingappbr;


import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.bakingappbr.adapter.EachRecipeAdapter;

import com.example.android.bakingappbr.databinding.FragmentRecipeEachBinding;
import com.example.android.bakingappbr.model.IngredientItem;
import com.example.android.bakingappbr.model.RecipeStep;
import com.example.android.bakingappbr.model.RecipesItem;
import com.example.android.bakingappbr.presenter.EachRecipeContract;
import com.example.android.bakingappbr.presenter.EachRecipePresenter;
import com.example.android.bakingappbr.util.OnItemClickListener;

import java.util.ArrayList;



public class EachRecipeFragment extends Fragment implements EachRecipeContract.View{
    private final String TAG="XXXX_fragment";
    public static final String EACHRECIPE_SAVE = "EachRecipe_Save ";

    private final String ADAPTER_CLICK_POSITION = "adapter_click_position";
    private final String RECIPES= "recipes";

    private final String INGREDIENTS_COUNT = "ingredients_count";
    private final String RECIPE_STEPS = "recipe_steps";



    private FragmentRecipeEachBinding binding;

    private RecipesItem mRecipes;
    private EachRecipePresenter mEachRecipePresenter;
    private RecyclerView mStepRecyclerView;
    private EachRecipeAdapter mEachRecipeAdapter;
    private int mRecipeStepAdapterSavedPosition = 0;
    private ArrayList<RecipeStep> mRecipeStepList = new ArrayList<>();
    private ArrayList<TextView> mIngredientList = new ArrayList<>();




    public static EachRecipeFragment newInstance(RecipesItem recipes) {
        Bundle args = new Bundle();
        args.putParcelable(EACHRECIPE_SAVE, recipes);
        EachRecipeFragment fragment = new EachRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();


        if ((arguments != null) &&(arguments.containsKey(EACHRECIPE_SAVE))) {
            mRecipes = arguments.getParcelable(EACHRECIPE_SAVE);

        }

        if (savedInstanceState != null &&savedInstanceState.containsKey(ADAPTER_CLICK_POSITION)) {
            mRecipeStepAdapterSavedPosition = savedInstanceState.getInt(ADAPTER_CLICK_POSITION);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPES)) {
                mRecipes = savedInstanceState.getParcelable(RECIPES);
            }

            if (savedInstanceState.containsKey(RECIPE_STEPS)) {
                mRecipeStepList = savedInstanceState.getParcelableArrayList(RECIPE_STEPS);
            }

            if (savedInstanceState.containsKey(INGREDIENTS_COUNT)) {
                int ingredientCount = savedInstanceState.getInt(INGREDIENTS_COUNT);
                for (int i = 0; i < ingredientCount; i++) {
                    TextView mTextView = new TextView (this.getContext());
                    mTextView .setText(String.valueOf(mRecipes.getIngredients().get(i).getQuantity()) +
                            String.valueOf(mRecipes.getIngredients().get(i).getMeasure()) + " " + mRecipes.getIngredients().get(i).getIngredient());

                    mIngredientList.add(mTextView );
                }
            }
        }

        mEachRecipePresenter = new EachRecipePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_each, container, false);
        final View view = binding.getRoot();


        if (mIngredientList.size() > 0) {
            for (TextView  mIngredientView : mIngredientList) {
                binding.ingredientList.addView(mIngredientView);
            }
        }else{
            if (mRecipes.getIngredients() != null && mRecipes.getIngredients().size() > 0) {
                for (IngredientItem ingredientItem : mRecipes.getIngredients()) {
                    TextView  mTextView  = new TextView (this.getContext());
                    mTextView .setText(String.valueOf(ingredientItem.getQuantity()) +
                            String.valueOf(ingredientItem.getMeasure()) + " " + ingredientItem.getIngredient());
                    mTextView .setTextColor(Color.parseColor("#FFFFFF"));
                    binding.ingredientList.addView(mTextView );
                    mIngredientList.add(mTextView );

                }
            }
        }
        if (mRecipes.getSteps() != null && mRecipes.getSteps().size() > 0 && mRecipeStepList.size() == 0) {
            mRecipeStepList.addAll(mRecipes.getSteps());
        }


        mStepRecyclerView = (RecyclerView) view.findViewById(R.id.steps_recycler_view);
        mStepRecyclerView.setHasFixedSize(true);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mEachRecipeAdapter = new EachRecipeAdapter(mRecipeStepList, new OnItemClickListener<RecipeStep>() {

            @Override
            public void onClick(RecipeStep recipeStep, View view) {
                mEachRecipePresenter.getEachRecipe(mRecipeStepList, recipeStep.getId(), mRecipes.getName(), view);
            }
        });

        mEachRecipeAdapter.setStepAdapterCurrentPosition(mRecipeStepAdapterSavedPosition);
        mStepRecyclerView.setAdapter(mEachRecipeAdapter);
        return view;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ADAPTER_CLICK_POSITION, mEachRecipeAdapter.getStepAdapterCurrentPosition());
        outState.putParcelable(RECIPES, mRecipes);
        outState.putParcelableArrayList(RECIPE_STEPS, mRecipeStepList);



    }




}
