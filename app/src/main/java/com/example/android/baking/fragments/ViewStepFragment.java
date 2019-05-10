package com.example.android.baking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.adapters.StepAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ViewStepFragment extends Fragment {

    @BindView(R.id.media_player_view) ImageView mMediaPlayerView;
    @BindView(R.id.step_instruction) TextView mInstructionTextView;
    @BindView(R.id.next_step) ImageButton mNextStepButton;
    @BindView(R.id.previous_step) ImageButton mPreviousStepButton;

    private Step mStep;
    private Step[] mSteps;
    private int mStepPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_step, container, false);
        ButterKnife.bind(this, view);

        mNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextStep();
            }
        });

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

        mInstructionTextView.setText(mStep.getDescription());

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
        // TODO set buttons
    }




}
