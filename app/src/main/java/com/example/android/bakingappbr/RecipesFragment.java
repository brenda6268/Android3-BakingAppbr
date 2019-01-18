package com.example.android.bakingappbr;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingappbr.adapter.Mainadapter;
import com.example.android.bakingappbr.databinding.FragmentRecipeListBinding;
//import com.example.android.bakingappbr.databinding.ToolbarBinding;
import com.example.android.bakingappbr.model.RecipesItem;
import com.example.android.bakingappbr.netData.RecipesItemService;
import com.example.android.bakingappbr.presenter.RecipesContract;
import com.example.android.bakingappbr.presenter.RecipesPresenter;
import com.example.android.bakingappbr.util.OnItemClickListener;


import java.util.ArrayList;




public class RecipesFragment extends Fragment implements RecipesContract.View{
    private static final String RECIPES_SAVE_KEY = "recipes_save_key";
     private RecipesContract.Presenter mRecipesPresenter;
     private ArrayList<RecipesItem> mRecipeList=new ArrayList<>();
     private Mainadapter mMainadapter;
     private RecyclerView mRecipesRecyclerView;
     private FragmentRecipeListBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(RECIPES_SAVE_KEY)) {
                mRecipeList = savedInstanceState.getParcelableArrayList(RECIPES_SAVE_KEY);
            }
        }
    }



    public RecipesFragment() {
    }


    private static final String TAG = RecipesFragment.class.getSimpleName();



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false);
            final View view = binding.getRoot();


            mRecipesRecyclerView = (RecyclerView) view.findViewById(R.id.recipe_recycler_view);
            mRecipesRecyclerView.setHasFixedSize(true);


           // if (isPad(getContext())) {mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
            //        3));}
            int mNoOfColumns = calculateNoOfColumns(getContext());
            mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),
                    mNoOfColumns));

            mRecipesPresenter= new RecipesPresenter(this, new RecipesItemService());
            mMainadapter = new Mainadapter(mRecipeList, new OnItemClickListener<RecipesItem>() {

                @Override
                public void onClick(RecipesItem recipe, View view) {
                    mRecipesPresenter.recipeChosen(recipe, view);
                }
            });

            mRecipesRecyclerView.setAdapter(mMainadapter);

            if (mRecipeList == null || mRecipeList.size() == 0) {
                mRecipesPresenter.getRecipes();
            }
            return view;
        }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        String FEED_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

        Log.d(TAG, "begin url" + FEED_URL);
        //Start download

        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public void refreshUI(ArrayList<RecipesItem> recipeList) {
        mRecipeList.clear();
        mRecipeList.addAll(recipeList);
        mMainadapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String string) {
        //change UI
    }

@Override
public boolean isActive() {
    return isAdded();
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RECIPES_SAVE_KEY, mRecipeList);
        super.onSaveInstanceState(outState);
    }

    public static RecipesFragment newInstance( ) {
        Bundle args = new Bundle();
        //args.putString("name", name);
        RecipesFragment fragment = new RecipesFragment();
        fragment.setArguments(args);
        return fragment;
    }


//column number of gridlayout manger
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 360);
        return noOfColumns;
    }

}
