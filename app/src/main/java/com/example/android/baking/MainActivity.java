package com.example.android.baking;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.android.baking.adapters.RecipeAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.fragments.SelectRecipeFragment;
import com.example.android.baking.fragments.SelectStepFragment;
import com.example.android.baking.fragments.ViewStepFragment;
import com.example.android.baking.utils.DeviceUtils;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        SelectRecipeFragment.OnRecipeClickListener,
        SelectStepFragment.OnStepClickListener{

    private boolean twoPane;
    private FragmentManager mFragmentManager;
    private SelectStepFragment mSelectStepFragment;
    private SelectRecipeFragment mSelectRecipeFragment;
    private ViewStepFragment mViewStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Timber
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        // Create fragments
        if (mFragmentManager == null) mFragmentManager = getSupportFragmentManager();
        if (mSelectRecipeFragment == null) mSelectRecipeFragment = new SelectRecipeFragment();
        if (mSelectStepFragment == null) mSelectStepFragment = new SelectStepFragment();
        if (mViewStepFragment == null) mViewStepFragment = new ViewStepFragment();

        // Handle set up for one pane or two pane layout
        if (DeviceUtils.isDeviceTablet()) twoPane = true;
        if (twoPane){
            // TODO handle two pane layout
        }
        else {
            // TODO handle one pane layout
            mFragmentManager.beginTransaction()
                    .replace(R.id.main_activity_fragment_container, mSelectRecipeFragment)
                    .commit();
        }
    }


    @Override
    public void onRecipeSelected(Recipe recipe) {
        // Set the recipe information for the fragment
        mSelectStepFragment.setRecipe(recipe);
        mFragmentManager.beginTransaction()
                .replace(R.id.main_activity_fragment_container, mSelectStepFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onStepSelected(Step step) {
        // TODO handle step selection
    }

}
