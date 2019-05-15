package com.example.android.baking.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.android.baking.R;
import com.example.android.baking.data.Step;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewStepFragment extends Fragment {

    @BindView(R.id.media_player_view) PlayerView mMediaPlayerView;
    @BindView(R.id.step_instruction) TextView mInstructionTextView;
    @BindView(R.id.next_step) ImageButton mNextStepButton;
    @BindView(R.id.previous_step) ImageButton mPreviousStepButton;

    private Step mStep;
    private Step[] mSteps;
    private int mStepPosition;

    private ExoPlayer mExoPlayer;

    //TODO handle rotation

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_step, container, false);
        ButterKnife.bind(this, view);

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        initializePlayer();
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
            // TODO set default drawable
            //mMediaPlayerView.setDefaultArtwork();
            mExoPlayer.setPlayWhenReady(true);
            //mExoPlayer.seekTo(currentWindow, playbackPosition);
            updatePlayer();
        }

    }

    private void releasePlayer(){
        if (mExoPlayer!=null){
            //playbackPosition = mExoPlayer.getCurrentPosition();
            //currentWindow = player.getCurrentWindowIndex();
            //playWhenRead = player.getPlayWhenReady();
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
                mExoPlayer.prepare(mediaSource, true, false);
            } else if (!thumbnail.equals("")){
                MediaSource mediaSource = buildMediaSource(Uri.parse(thumbnail));
                mExoPlayer.prepare(mediaSource, true, false);
            } else {
                //mExoPlayer.release();
                // TODO handle no video or thumbnail
                //mMediaPlayerView.setDefaultArtwork(getResources().getDrawable(R.drawable.exo_controls_pause));
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

    private void updateUI(){
        // Set the description
        mInstructionTextView.setText(mStep.getDescription());

        // Get the urls
        String thumbnailURL = mStep.getThumbnailURL();
        String videoURL = mStep.getVideoURL();

        updatePlayer();

        Toast toast;
        String toastText = "desc: ";
        if (!videoURL.equals(""))
            toastText += "+video";
        if (!thumbnailURL.equals(""))
            toastText += "+thumb";
        toast = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
        toast.show();
    }
}
