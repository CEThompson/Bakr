package com.example.android.baking.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewStepFragment extends Fragment {

    @BindView(R.id.media_player_view) PlayerView mMediaPlayerView;
    @BindView(R.id.step_instruction) TextView mInstructionTextView;
    @BindView(R.id.next_step) ImageButton mNextStepButton;
    @BindView(R.id.previous_step) ImageButton mPreviousStepButton;
    @BindView(R.id.player_overlay) ImageView mExoOverlay;
    @BindView(R.id.player_container) FrameLayout mPlayerContainer;
    @BindView(R.id.video_progress_bar) ProgressBar mProgressBar;

    private Step mStep;
    private Step[] mSteps;
    private int mStepPosition;

    private ExoPlayer mExoPlayer;

    private long mPlaybackPosition;
    private boolean mPlayWhenReady;
    private int mCurrentWindow;

    private static final String KEY_PLAYBACK_POSITION = "playback_position";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_CURRENT_WINDOW = "current_window";
    private static final String KEY_STEP_POSITION = "step_position";

    private boolean navHidden;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_step, container, false);
        ButterKnife.bind(this, view);

        // For portrait and video landscape
        navHidden = false;

        // Initialize playback variables
        mPlaybackPosition = 0;
        mCurrentWindow = 0;
        mPlayWhenReady = true;

        // Restore playback variables if necessary
        if (savedInstanceState!=null){
            mPlaybackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            mStepPosition = savedInstanceState.getInt(KEY_STEP_POSITION);
        }

        /* If tablet get rid of next and previous buttons */
        if (getResources().getBoolean(R.bool.is_600_wide)) {
            mPreviousStepButton.setVisibility(View.INVISIBLE);
            mNextStepButton.setVisibility(View.INVISIBLE);
            navHidden = true;
        }

        /* If not tablet handle here */
        else {
            int orientation = getResources().getConfiguration().orientation;

            /* If phone is oriented in landscape fill screen with player */
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                // NOTE: Hide the action bar is checked in activity

                // Hide the nav buttons in landscape
                mNextStepButton.setVisibility(View.INVISIBLE);
                mPreviousStepButton.setVisibility(View.INVISIBLE);
                navHidden = true;

                // Set the resize mode to fill
                mMediaPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }

            /* Handle phone in portrait orientation */
            else {
                // TODO is there a way to do this without hardcoding?
                // Resize the player view by aspect ratio and device width
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mMediaPlayerView.getLayoutParams();
                params.width = getResources().getDisplayMetrics().widthPixels;
                params.height = params.width * 9 / 16; // 9 /16 is aspect ratio
                mMediaPlayerView.setLayoutParams(params);
                Timber.d("Width is %s1, height is %s2", params.width, params.height);


                // Set up listener for next step button
                mNextStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextStep();
                    }
                });

                // Set listener for previous step button
                mPreviousStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        previousStep();
                    }
                });
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
        updateUI();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURRENT_WINDOW, mCurrentWindow);
        outState.putLong(KEY_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putBoolean(KEY_PLAY_WHEN_READY, mPlayWhenReady);
        outState.putInt(KEY_STEP_POSITION, mStepPosition);
    }

    private void initializePlayer(){
        if (mExoPlayer == null){
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());

            mMediaPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
        }

    }

    private void releasePlayer(){
        if (mExoPlayer!=null){
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updatePlayer(){
        if (mExoPlayer!=null) {
            // Get the relevant urls
            String URL = mStep.getVideoURL();
            String thumbnail = mStep.getThumbnailURL();

            // Stop player and reset positioning
            mExoPlayer.stop();
            mCurrentWindow = 0;
            mPlaybackPosition = 0;

            // If there is a video url display it
            if (!URL.equals("")) {
                MediaSource mediaSource = buildMediaSource(Uri.parse(URL));
                mExoPlayer.prepare(mediaSource, true, false);
                showPlayer();
            }
            // If there is a thumbnail url display it
            else if (!thumbnail.equals("")){
                MediaSource mediaSource = buildMediaSource(Uri.parse(thumbnail));
                mExoPlayer.prepare(mediaSource, true, false);
                showPlayer();
            }
            // Otherwise indicate that there is neither
            else {
                showPlaceholder();
            }
        }
    }

    /* Builds a media source from a uri for exoplayer*/
    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);
    }

    /* Sets the steps for the fragment */
    public void setSteps(Step[] steps){
        mSteps = steps; }

    /* Sets the current step position */
    public void setStepPosition(int position){
        mStepPosition = position;
        mStep = mSteps[mStepPosition]; }

    /* Increments the step position */
    private void nextStep(){
        // Increment only if below last step
        if (mStepPosition < mSteps.length-1){
            mStepPosition++;
            mStep = mSteps[mStepPosition];
            updateUI();
        }
    }

    /* Decrements the step position */
    private void previousStep(){
        // Step is greater than zero so decrement it
        if (mStepPosition > 0){
            mStepPosition--;
            mStep = mSteps[mStepPosition];
            updateUI();
        }
    }

    /* Sets the instructions, triggers nav control and updates player */
    public void updateUI(){
        // Set the description and update the player
        mInstructionTextView.setText(mStep.getDescription());
        updateNavButtons();
        updatePlayer();
    }

    /* Handles the nav buttons depending upon orientation */
    private void updateNavButtons(){
        // only handle if flagged
        if (!navHidden){
            if (mStepPosition==0) mPreviousStepButton.setVisibility(View.INVISIBLE);
            else if (mStepPosition == mSteps.length-1) mNextStepButton.setVisibility(View.INVISIBLE);
            else {
                mPreviousStepButton.setVisibility(View.VISIBLE);
                mNextStepButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showPlayer(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mMediaPlayerView.setVisibility(View.VISIBLE);
        mExoOverlay.setVisibility(View.INVISIBLE);
    }

    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
        mMediaPlayerView.setVisibility(View.INVISIBLE);
        mExoOverlay.setVisibility(View.INVISIBLE);
    }

    private void showPlaceholder(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mMediaPlayerView.setVisibility(View.INVISIBLE);
        mExoOverlay.setVisibility(View.VISIBLE);
    }
}
