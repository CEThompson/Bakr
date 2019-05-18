package com.example.android.baking;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.fragments.SelectRecipeFragment;
import com.example.android.baking.test.SimpleIdlingResource;

import butterknife.BindView;
import timber.log.Timber;

public class RecipeActivity extends AppCompatActivity implements
        SelectRecipeFragment.OnRecipeClickListener{

    @BindView(R.id.recipe_recyclerview)
    RecyclerView mRecipeRecyclerView;
    @BindView(R.id.select_recipe_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.recipe_errorview)
    TextView mErrorView;

    private SelectRecipeFragment mSelectRecipeFragment;

    public static final String RECIPE_KEY = "recipe";
    public static final String SELECT_RECIPE_FRAGMENT_KEY = "select_recipe_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Restore state for rotation, etc.
        if (savedInstanceState!=null){
            mSelectRecipeFragment = (SelectRecipeFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, SELECT_RECIPE_FRAGMENT_KEY);
            Timber.d("restoring select recipe fragment %s", mSelectRecipeFragment.getId());
        }

        // Set up Timber
        if (BuildConfig.DEBUG){
            if (savedInstanceState==null)
                Timber.plant(new Timber.DebugTree());
        }

        // Set screen orientation for tablet
        if(getResources().getBoolean(R.bool.is_600_wide)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Create fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mSelectRecipeFragment == null) mSelectRecipeFragment = new SelectRecipeFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.main_activity_fragment_container, mSelectRecipeFragment)
                .commit();

    }

    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, StepsActivity.class);
        intent.putExtra(RECIPE_KEY, recipe); // Send the recipe in bundle
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, SELECT_RECIPE_FRAGMENT_KEY, mSelectRecipeFragment);
    }
}
