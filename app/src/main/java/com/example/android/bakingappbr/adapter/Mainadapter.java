package com.example.android.bakingappbr.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;

import com.example.android.bakingappbr.R;
import com.example.android.bakingappbr.databinding.RecipeListItemBinding;
import com.example.android.bakingappbr.model.RecipesItem;

import com.example.android.bakingappbr.util.OnItemClickListener;
import java.util.ArrayList;

public class Mainadapter extends RecyclerView.Adapter<Mainadapter.RecipesViewHolder> {
    private ArrayList<RecipesItem> mRecipeList;
    private OnItemClickListener mRecipeOnClick;
    private LayoutInflater inflater;


    public Mainadapter( ArrayList<RecipesItem> mRecipeList, OnItemClickListener<RecipesItem> recipeOnClick) {
        this.mRecipeList = mRecipeList;

        this.mRecipeOnClick=recipeOnClick;
    }



    @Override
    public Mainadapter.RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(parent.getContext());

        RecipeListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.recipe_list_item, parent, false);

        return new RecipesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(Mainadapter.RecipesViewHolder holder, int position) {
        holder.mRecipe = mRecipeList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(holder.mRecipe.getImage())
                .placeholder(R.drawable.dish)
                .error(R.drawable.dish)
                .dontAnimate()
                .into(holder.mBinding.circleRecipeImage);
        holder.mBinding.mainRecipeTitle.setText(holder.mRecipe.getName());
        holder.mBinding.mainRecipeTitle.setSelected(true);
        holder.mBinding.mainRecipeTitle.setHorizontallyScrolling(true);
      }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecipeListItemBinding mBinding;
        private RecipesItem mRecipe;

        public RecipesViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            mBinding.mainRecipeList.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeOnClick.onClick(mRecipe, v);
        }
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

}
