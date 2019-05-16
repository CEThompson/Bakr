package com.example.android.baking;

import android.app.AppComponentFactory;
import android.app.NotificationManager;
import android.content.pm.ActivityInfo;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.fragments.SelectStepFragment;
import com.example.android.baking.fragments.ViewStepFragment;
import com.google.android.exoplayer2.ExoPlayer;

import timber.log.Timber;

public class StepsActivity extends AppCompatActivity implements
        SelectStepFragment.OnStepClickListener {

    private boolean twoPane;

    private FragmentManager mFragmentManager;
    private SelectStepFragment mSelectStepFragment;
    private ViewStepFragment mViewStepFragment;

    // TODO implement media session
    private ExoPlayer mExoPlayer;
    private MediaSession mMediaSession;
    private NotificationManager mNotificationManager;
    private PlaybackState.Builder mStateBuilder;

    private Recipe mRecipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        // Set to landscape orientation for tablets
        if(getResources().getBoolean(R.bool.is_tablet)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        // Create fragments
        if (mFragmentManager == null) mFragmentManager = getSupportFragmentManager();
        if (mSelectStepFragment == null) mSelectStepFragment = new SelectStepFragment();
        if (mViewStepFragment == null) mViewStepFragment = new ViewStepFragment();

        if (getIntent().hasExtra(RecipeActivity.RECIPE_KEY)){
            mRecipe = getIntent().getParcelableExtra(RecipeActivity.RECIPE_KEY);
            mSelectStepFragment.setRecipe(mRecipe);
            mViewStepFragment.setSteps(mRecipe.getSteps());
            mViewStepFragment.setStepPosition(0); // initialize step
        }

        // Handle set up for one pane or two pane layout
        //if (DeviceUtils.isDeviceTablet()) twoPane = true;
        twoPane = (findViewById(R.id.steps_single_pane_container)==null);

        Timber.d("Two pane is: " + twoPane);
        if (twoPane){
            mFragmentManager.beginTransaction()
                    .replace(R.id.steps_two_pane_left, mSelectStepFragment)
                    .commit();
            mFragmentManager.beginTransaction()
                    .replace(R.id.steps_two_pane_right, mViewStepFragment)
                    .commit();
        }
        else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.steps_single_pane_container, mSelectStepFragment)
                    .commit();
        }

    }

    @Override
    public void onStepSelected(int position) {
        // Send the selected position to the fragment
        mViewStepFragment.setStepPosition(position);

        // Swap out the fragment depending upon id
        if (twoPane) {
            mViewStepFragment.updateUI();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.steps_single_pane_container, mViewStepFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

}
