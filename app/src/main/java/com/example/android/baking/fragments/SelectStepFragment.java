package com.example.android.baking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.RecipeActivity;
import com.example.android.baking.adapters.IngredientAdapter;
import com.example.android.baking.adapters.StepAdapter;
import com.example.android.baking.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SelectStepFragment extends Fragment implements StepAdapter.StepOnClickHandler{

    @BindView(R.id.step_recyclerview)
    RecyclerView mStepRecyclerView;
    @BindView(R.id.ingredients_listview)
    RecyclerView mIngredientsRecyclerView;
    private StepAdapter mStepAdapter;
    private IngredientAdapter mIngredientAdapter;
    private OnStepClickListener mCallback;
    private Recipe mRecipe;

    public interface OnStepClickListener {
        void onStepSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (Exception e){
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Restore
        if (savedInstanceState!= null){
            mRecipe = savedInstanceState.getParcelable(RecipeActivity.RECIPE_KEY);
        }
        /* Set the data for the steps */
        // Create and set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mStepRecyclerView.setLayoutManager(layoutManager);
        mStepRecyclerView.setHasFixedSize(true);

        // Create the adapter
        mStepAdapter = new StepAdapter(this);

        // Set the adapter on the recycler view
        mStepRecyclerView.setAdapter(mStepAdapter);

        /* Set the data for the ingredients */
        LinearLayoutManager ingredientManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientAdapter = new IngredientAdapter(getContext());
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        // Set the data on the recycler views
        mStepAdapter.setmRecipeData(mRecipe.getSteps());
        mIngredientAdapter.setData(mRecipe.getIngredients());

        // When resuming this fragment show the action bar
        try {((AppCompatActivity) getActivity()).getSupportActionBar().show();}
        catch (Exception e) { Timber.d(e); }
    }

    @Override
    public void onClick(int position) {
        mCallback.onStepSelected(position);
        Timber.d("selecting step");
    }

    public void setRecipe(Recipe recipe){
        mRecipe = recipe;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RecipeActivity.RECIPE_KEY, mRecipe);
    }
}
