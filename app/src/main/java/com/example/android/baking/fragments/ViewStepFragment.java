package com.example.android.baking.fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.android.baking.R;
import com.example.android.baking.adapters.StepAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewStepFragment extends Fragment {

    @BindView(R.id.media_player_view) PlayerView mMediaPlayerView;
    @BindView(R.id.step_instruction) TextView mInstructionTextView;
    @BindView(R.id.next_step) ImageButton mNextStepButton;
    @BindView(R.id.previous_step) ImageButton mPreviousStepButton;

    private Step mStep;
    private Step[] mSteps;
    private int mStepPosition;

    private ExoPlayer mExoPlayer;

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

    }

    public void setSteps(Step[] steps){mSteps = steps;}

    public void setStepPosition(int position){
        mStepPosition = position;
        setStep();
    }

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
        // TODO set media player content
        mInstructionTextView.setText(mStep.getDescription());

        //mMediaPlayerView.setDefaultArtwork();
        String thumbnailURL = mStep.getThumbnailURL();
        String videoURL = mStep.getVideoURL();

        Toast toast;
        if (!videoURL.equals(""))
            toast = Toast.makeText(getActivity(), "videoURL:"+videoURL, Toast.LENGTH_SHORT);
        else if (!thumbnailURL.equals(""))
            toast = Toast.makeText(getActivity(), "thumbnailURL:"+thumbnailURL, Toast.LENGTH_SHORT);
        else
            toast = Toast.makeText(getActivity(), "no video or thumbnail", Toast.LENGTH_SHORT);

        toast.show();

        // Set up image
        /*
        if (videoURL.isEmpty()) {
            Glide.with(this)
                    .load(thumbnailURL)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            mMediaPlayerView.setDefaultArtwork(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            mMediaPlayerView.setDefaultArtwork(getResources().getDrawable(R.drawable.ic_error_black_24dp));
                        }
                    });
        }
        // Set up video
        else {
            // TODO set up video
            Glide.with(this)
                    .load(thumbnailURL)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            mMediaPlayerView.setDefaultArtwork(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            mMediaPlayerView.setDefaultArtwork(getResources().getDrawable(R.drawable.ic_error_black_24dp));
                        }
                    });
        }
        */
        // TODO set buttons
    }




}
