package com.example.android.bakingappbr.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.example.android.bakingappbr.R;
import com.example.android.bakingappbr.databinding.RecipeStepListItemBinding;
import com.example.android.bakingappbr.model.RecipeStep;
import com.example.android.bakingappbr.util.OnItemClickListener;
import java.util.ArrayList;

public class EachRecipeAdapter extends RecyclerView.Adapter<EachRecipeAdapter.EachRecipeViewHolder> {
    private ArrayList<RecipeStep> mRecipeStep;
    private OnItemClickListener mRecipeStepOnClick;
    private int mClickPostion=0;
    private Context context;
    private LayoutInflater inflater;

    public EachRecipeAdapter(ArrayList<RecipeStep> Step, OnItemClickListener<RecipeStep> OnClickstep) {
        this.mRecipeStep = Step;
        this.mRecipeStepOnClick = OnClickstep;
    }

    @Override
    public EachRecipeAdapter.EachRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_list_item, null);
        RecipeStepListItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.recipe_step_list_item, parent, false);

        return new EachRecipeAdapter.EachRecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(EachRecipeAdapter.EachRecipeViewHolder holder, int position) {
        holder.mRecipeStep = mRecipeStep.get(position);
        holder.mBinding.stepItemHolder.setSelected(mClickPostion == position);



        if (holder.mRecipeStep.getVideoURL() == null||holder.mRecipeStep.getVideoURL().matches("")) {
            holder.mBinding.circleVideoThumb.setImageResource(R.drawable.novideo);
            holder.mBinding.stepNumber.setText("No Video"+String.valueOf(holder.mRecipeStep.getId()) + ": ");
        }
        else {

            Glide.with(holder.itemView.getContext())
                    .load(holder.mRecipeStep.getThumbnailURL())
                    .placeholder(R.drawable.video)
                    .error(R.drawable.video)
                    .dontAnimate()
                    .into(holder.mBinding.circleVideoThumb);
           // holder.mBinding.tvStepListStepNumber.setText(String.valueOf(holder.mRecipeStep.getId()) + ": ");
        }
        holder.mBinding.stepNumber.setText(String.valueOf(holder.mRecipeStep.getId()) + ": ");
        holder.mBinding.stepShortDesc.setText(holder.mRecipeStep.getShortDescription());

    }

    public class EachRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final RecipeStepListItemBinding mBinding;
        private RecipeStep mRecipeStep;

        public EachRecipeViewHolder(RecipeStepListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            mBinding.stepItemHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mRecipeStepOnClick.onClick(mRecipeStep, v);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeStep==null){
             return 0;
        }
        return mRecipeStep.size();
    }

    // next to use
    public int getStepAdapterCurrentPosition() {
        return mClickPostion;
    }

    public void setStepAdapterCurrentPosition(int savedPosition) {
        mClickPostion= savedPosition;
    }



}
