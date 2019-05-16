package com.example.android.baking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.RecipeActivity;
import com.example.android.baking.adapters.RecipeAdapter;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.services.GetRecipesService;
import com.example.android.baking.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SelectRecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recipe_recyclerview) RecyclerView mRecipeRecyclerView;
    @BindView(R.id.select_recipe_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.recipe_errorview) TextView mErrorView;
    private Recipe[] mRecipes;
    private RecipeAdapter mRecipeAdapter;
    private OnRecipeClickListener mCallback;

    private final static int NUMBER_COLUMNS = 3;

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (Exception e){
            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_recipe, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Restore instance state */
        if (savedInstanceState!=null){
            /* Retrieve recipes ONLY if they are in the bundle! */
            ArrayList<Recipe> recipeList = savedInstanceState.getParcelableArrayList(RecipeActivity.RECIPE_KEY);
            if (recipeList !=null) {
                mRecipes = new Recipe[recipeList.size()];
                for (int i = 0; i < recipeList.size(); i++) {
                    mRecipes[i] = recipeList.get(i);
                }
            }
            /* Debugging */
            Timber.d("attempting to recover recipes: mRecipes is null == %s", mRecipes == null);
            try {Timber.d("mRecipes length is %s", mRecipes.length);}
            catch (Exception e) { Timber.e(e); }
        }

        /* Set the recycler view*/
        // If it is a tablet set up a grid
        if (getResources().getBoolean(R.bool.is_600_wide)){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUMBER_COLUMNS);
            mRecipeRecyclerView.setLayoutManager(gridLayoutManager);
        }
        // Otherwise set up a linear layout
        else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            mRecipeRecyclerView.setLayoutManager(layoutManager);
        }

        mRecipeRecyclerView.setHasFixedSize(true);

        // Create the adapter
        mRecipeAdapter = new RecipeAdapter(this, getContext());

        // Set the adapter on the recycler view
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        // Async call to get recipe data if the recipes haven't already been saved
        if (mRecipes == null) {
            getRecipes();
            Timber.d("querying recipes from server");
        }
        // Otherwise set it from saved data
        else {
            mRecipeAdapter.setmRecipeData(mRecipes);
            showRecipes();
            Timber.d("setting recipes from saved instance state");
        }
    }

    /* Async retrofit call gets the recipes as a json object and turns them into relevant objects */
    private void getRecipes(){
        GetRecipesService recipesService = NetworkUtils.getRetrofitInstance().create(GetRecipesService.class);
        Call<Recipe[]> call = recipesService.getRecipes();
        call.enqueue(new Callback<Recipe[]>() {
            /* Success hides irrelevant views, sets the adapter data, and logs */
            @Override
            public void onResponse(Call<Recipe[]> call, Response<Recipe[]> response) {
                mRecipes = response.body();
                mRecipeAdapter.setmRecipeData(mRecipes);
                showRecipes();
                Timber.d("Retrofit success, Recipe[] length is %s.", response.body().length);
            }
            /* Failure shows the error message and logs */
            @Override
            public void onFailure(Call<Recipe[]> call, Throwable t) {
                showError();
                Timber.d("Retrofit failure, message is: %s.", t.getMessage());
            }
        });
    }

    /* sends the selected recipe to main activity */
    @Override
    public void onClick(Recipe clickedRecipe) {
        mCallback.onRecipeSelected(clickedRecipe);
        Timber.d("click logging in select recipe fragment");
    }

    /* Shows the recipes */
    private void showRecipes(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.INVISIBLE);
    }

    /* Shows the error view */
    private void showError(){
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        /* Save the recipes on rotation BUT only if the async call was successful! */
        if (mRecipes!=null) {
            ArrayList<Recipe> recipeList = new ArrayList<>();
            for (int i = 0; i < mRecipes.length; i++) {
                recipeList.add(mRecipes[i]);
            }
            outState.putParcelableArrayList(RecipeActivity.RECIPE_KEY, recipeList);
        }
    }
}
