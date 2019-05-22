package com.example.android.baking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.android.baking.R;
import com.example.android.baking.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder> {

    private Step[] mStepData;
    private final StepOnClickHandler mClickHandler;

    public interface StepOnClickHandler{
        void onClick(int position);
    }

    public StepAdapter(StepOnClickHandler handler){ mClickHandler = handler; }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener{
        @BindView(R.id.step_id) TextView mId;
        @BindView(R.id.step_short_description) TextView mShortDescription;

        public StepAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //Step clickedStep = mStepData[position];
            mClickHandler.onClick(position);
            Timber.d("clicking position " + position);
        }
    }

    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipe_step_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);

        return new StepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder holder, int position) {
        Step currentStep = mStepData[position];

        int id = currentStep.getId();
        String shortDescription = currentStep.getShortDescription();

        holder.mId.setText(String.valueOf(id));
        holder.mShortDescription.setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if (mStepData==null)return 0;
        return mStepData.length;
    }

    public void setmRecipeData(Step[] steps){
        mStepData = steps;
        notifyDataSetChanged();
        Timber.d("Setting step data");
    }
}
