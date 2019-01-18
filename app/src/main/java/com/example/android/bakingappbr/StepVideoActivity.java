package com.example.android.bakingappbr;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingappbr.adapter.TabViewPagerAdapter;
import com.example.android.bakingappbr.databinding.ActivityStepVideoBinding;
import com.example.android.bakingappbr.model.RecipeStep;

import java.util.ArrayList;


import static android.view.View.GONE;

public class StepVideoActivity extends AppCompatActivity {
    private static final String STEP_ALL_SAVE = "step_all_save ";
    private static final String RECIPE_NAME_SAVE = "recipe_name_save";
    private static final String STEP_SAVE = "step_save";
    private ViewPager mViewPager;
    private ActivityStepVideoBinding mbinding;



    public static Intent SVAIntent(Context packageContext, ArrayList<RecipeStep> stepList,
                                   int currentStep, String recipeName) {
        Intent intent = new Intent(packageContext, StepVideoActivity.class);
        intent.putExtra(STEP_ALL_SAVE, stepList);
        intent.putExtra(STEP_SAVE, currentStep);
        intent.putExtra(RECIPE_NAME_SAVE, recipeName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_step_video);

        final ArrayList<RecipeStep> stepList = getIntent().getExtras().getParcelableArrayList(STEP_ALL_SAVE);
        final int currentStep = getIntent().getExtras().getInt(STEP_SAVE);

        String re_Name=getIntent().getExtras().getString(RECIPE_NAME_SAVE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(re_Name);

        //Tablayout
        /////TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_activity_step_viewpager);

        TabLayout tabLayout =(TabLayout) mbinding.stepTablayoutOne;
        mViewPager = (ViewPager)mbinding.stepViewpagerOne;
        /////       mViewPager = (ViewPager) findViewById(R.id.vp_activity_step_viewpager);
       // tabLayout.setupWithViewPager(mViewPager);
        for (int j=0; j<stepList.size();j++) {
            tabLayout.addTab(tabLayout.newTab().setText(
                    String.format(getString(R.string.step_number), j)));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            actionBar.hide();
            mbinding.stepTablayoutOne.setVisibility(GONE);
        }

       FragmentManager fm = getSupportFragmentManager();

         mViewPager.setAdapter(new TabViewPagerAdapter(fm, stepList));
         mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


       mViewPager.setCurrentItem(currentStep);

    }
}