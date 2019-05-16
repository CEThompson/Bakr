package com.example.android.baking;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.fragments.SelectRecipeFragment;

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

    private FragmentManager mFragmentManager;
    private SelectRecipeFragment mSelectRecipeFragment;

    public static final String RECIPE_KEY = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Set up Timber
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        // Set to landscape orientation for tablets
        if(getResources().getBoolean(R.bool.is_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Create fragments
        if (mFragmentManager == null) mFragmentManager = getSupportFragmentManager();
        if (mSelectRecipeFragment == null) mSelectRecipeFragment = new SelectRecipeFragment();

        mFragmentManager.beginTransaction()
                .replace(R.id.main_activity_fragment_container, mSelectRecipeFragment)
                .commit();

    }

    // TODO select recipe
    @Override
    public void onRecipeSelected(Recipe recipe) {
        Intent intent = new Intent(this, StepsActivity.class);
        // Send the recipe
        intent.putExtra(RECIPE_KEY, recipe);

        startActivity(intent);
        // TODO send the recipe to the activity

    }


}
