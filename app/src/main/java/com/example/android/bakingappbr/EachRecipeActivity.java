package com.example.android.bakingappbr;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


import com.example.android.bakingappbr.adapter.TabViewPagerAdapter;
import com.example.android.bakingappbr.model.RecipeStep;
import com.example.android.bakingappbr.model.RecipesItem;
import com.example.android.bakingappbr.presenter.EachRecipePresenter;
import java.util.ArrayList;


public class EachRecipeActivity extends AppCompatActivity implements EachRecipePresenter.Callbacks {
    public static final String EACHRECIPE_SAVE = "EachRecipe_Save ";
    private boolean mTwoPane;
    private ViewPager mViewPager;
    private ArrayList<RecipeStep> steps;
    public static Intent ERIntent(Context context, RecipesItem recipes) {
        Intent intent = new Intent(context, EachRecipeActivity.class);
        intent.putExtra(EACHRECIPE_SAVE, recipes);
        intent.putExtra("recipe_name",recipes.getName());
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_each);

      //get data from MainActivity
        RecipesItem recipes = getIntent().getExtras().getParcelable(EACHRECIPE_SAVE);
        steps=recipes.getSteps();
      //change action bar to show the name of selected recipe.
        String re_Name=recipes.getName();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(re_Name);

        //add fragment
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);


        if (fragment == null) {


            //one pane
            fragment = new EachRecipeFragment().newInstance(recipes);
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, "EachRecipeFragment")
                    .commit();

            //two pane
            if (findViewById(R.id.two_linear_layout) != null) {
                mTwoPane = true;

                //Tablayout
                TabLayout tabLayout = (TabLayout) findViewById(R.id.step_tablayout_two);
                mViewPager = (ViewPager) findViewById(R.id.step_viewpager_two);


                for (int j=0; j<steps.size();j++) {
                    tabLayout.addTab(tabLayout.newTab().setText(
                            String.format(getString(R.string.step_number), j)));
                }


                FragmentManager fm1 = getSupportFragmentManager();

                mViewPager.setAdapter(new TabViewPagerAdapter(fm1, steps));
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


                mViewPager.setCurrentItem(tabLayout.getSelectedTabPosition());


            }

        }
   }



    @Override
    public void EachStep(ArrayList<RecipeStep> stepList, int currentStep, String recipeName) {
        if (findViewById(R.id.two_linear_layout) != null) {
            Fragment addFragment = StepVideoFragment.SVFInstance(stepList.get(currentStep));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.add_video_container, addFragment)
                    .commit();
        }else{
        Intent intent = StepVideoActivity.SVAIntent(this, stepList, currentStep, recipeName);
        startActivity(intent);
        }
    }

}
