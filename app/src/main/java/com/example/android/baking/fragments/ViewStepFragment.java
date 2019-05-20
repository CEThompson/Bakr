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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
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
    private static final String KEY_CURRENT_WINDOW ="current_window";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_step, container, false);
        ButterKnife.bind(this, view);

        mPlaybackPosition = 0;
        mCurrentWindow = 0;
        mPlayWhenReady = true;
        if (savedInstanceState!=null){
            mPlaybackPosition = savedInstanceState.getLong(KEY_PLAYBACK_POSITION);
            mCurrentWindow = savedInstanceState.getInt(KEY_CURRENT_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
        }
        /* If tablet handle here */
        if (getResources().getBoolean(R.bool.is_600_wide)) {
            mNextStepButton.setVisibility(View.GONE);
            mPreviousStepButton.setVisibility(View.GONE);
        }
        /* If not tablet handle here */
        else {
            int orientation = getResources().getConfiguration().orientation;

            /* If phone is oriented in landscape fill screen with player */
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerContainer.getLayoutParams();
                params.width= FrameLayout.LayoutParams.MATCH_PARENT;
                params.height = FrameLayout.LayoutParams.MATCH_PARENT;
                mPlayerContainer.setLayoutParams(params);

                // Hide the nav buttons in landscape
                mNextStepButton.setVisibility(View.INVISIBLE);
                mPreviousStepButton.setVisibility(View.INVISIBLE);
            }
            /* Handle phone in portrait orientation */
            else {
                // TODO set size of video container in portrait orientation

                // Set back button
                mNextStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextStep();
                    }
                });

                // Set next button
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
    }

    private void initializePlayer(){
        if (mExoPlayer == null){
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());

            mMediaPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);
            mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

            // Show player when ready
            mExoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == Player.STATE_READY) showPlayer();
                    //if (playbackState == Player.STATE_ENDED) showPlayer();
                    //if (playbackState == Player.STATE_IDLE) //TODO do something when idle
                    if (playbackState == Player.STATE_BUFFERING) showLoading();
                }
            });

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
            String URL = mStep.getVideoURL();
            String thumbnail = mStep.getThumbnailURL();

            mExoPlayer.stop();

            mCurrentWindow = 0;
            mPlaybackPosition = 0;

            if (!URL.equals("")) {
                MediaSource mediaSource = buildMediaSource(Uri.parse(URL));
                mExoPlayer.prepare(mediaSource, false, false);
            } else if (!thumbnail.equals("")){
                MediaSource mediaSource = buildMediaSource(Uri.parse(thumbnail));
                mExoPlayer.prepare(mediaSource, false, false);
            } else {
                showPlaceholder();
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab"))
                .createMediaSource(uri);

    }

    public void setSteps(Step[] steps){
        mSteps = steps; }

    public void setStepPosition(int position){
        mStepPosition = position;
        setStep(); }

    private void setStep(){mStep = mSteps[mStepPosition]; }

    private void nextStep(){
        // Only go to next step if within array bounds
        if (mStepPosition < mSteps.length-1){
            mStepPosition++;
            mStep = mSteps[mStepPosition];
            updateUI();
        }
    }

    private void previousStep(){
        if (mStepPosition > 0){
            mStepPosition--;
            mStep = mSteps[mStepPosition];
            updateUI();
        }
    }

    public void updateUI(){
        // Set the description
        mInstructionTextView.setText(mStep.getDescription());
        updatePlayer();
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
