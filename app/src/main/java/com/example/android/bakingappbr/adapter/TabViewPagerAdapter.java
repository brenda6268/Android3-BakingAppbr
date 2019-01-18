package com.example.android.bakingappbr.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.bakingappbr.StepVideoFragment;
import com.example.android.bakingappbr.model.RecipeStep;

import java.util.ArrayList;


public class TabViewPagerAdapter extends FragmentStatePagerAdapter {
    private  FragmentManager fm;
    private ArrayList<RecipeStep> stepList;


    public TabViewPagerAdapter(FragmentManager fm, ArrayList<RecipeStep> stepList) {
        super(fm);
        this.stepList=stepList;
        this.fm=fm;


    }


    @Override
    public Fragment getItem(int position) {

        return StepVideoFragment.SVFInstance(stepList.get(position));
    }

    @Override
    public int getCount() {
        return stepList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(stepList.get(position).getId());
    }


}
