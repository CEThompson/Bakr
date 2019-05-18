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
import android.widget.Toast;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_step, container, false);
        ButterKnife.bind(this, view);

        // Initialize control variables
        mCurrentWindow = 0;
        mPlaybackPosition = 0;
        mPlayWhenReady = true;

        // If tablet get rid of next buttons
        if (getResources().getBoolean(R.bool.is_600_wide)) {
            mNextStepButton.setVisibility(View.GONE);
            mPreviousStepButton.setVisibility(View.GONE);
        }
        /* Handle view if not fragment */
        else {
            int orientation = getResources().getConfiguration().orientation;

            // TODO If landscape set video to take up screen
            if (orientation == Configuration.ORIENTATION_LANDSCAPE){
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mPlayerContainer.getLayoutParams();
                params.width= FrameLayout.LayoutParams.MATCH_PARENT;
                params.height = FrameLayout.LayoutParams.MATCH_PARENT;
                mPlayerContainer.setLayoutParams(params);
                //mMediaPlayerView.setLayoutParams(params);
                //mExoOverlay.setLayoutParams(params);
            }
            // Otherwise set by aspect ratio
            else {
                // TODO set size of video container?

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

        /*
        Toast toast;
        String toastText = "desc: ";
        if (!videoURL.equals(""))
            toastText += "+video";
        if (!thumbnailURL.equals(""))
            toastText += "+thumb";
        toast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
        toast.show();
        */
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
