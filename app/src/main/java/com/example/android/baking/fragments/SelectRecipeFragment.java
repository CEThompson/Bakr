package com.example.android.baking.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.adapters.RecipeAdapter;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.services.GetRecipesService;
import com.example.android.baking.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class SelectRecipeFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recipe_recyclerview) RecyclerView mRecipeRecyclerView;
    RecipeAdapter mRecipeAdapter;
    OnRecipeClickListener mCallback;

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

        // Create and set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setHasFixedSize(true);

        // Create the adapter
        mRecipeAdapter = new RecipeAdapter(this);

        // Set the adapter on the recycler view
        mRecipeRecyclerView.setAdapter(mRecipeAdapter);

        getRecipes();
    }

    private void getRecipes(){
        GetRecipesService recipesService = NetworkUtils.getRetrofitInstance().create(GetRecipesService.class);
        Call<Recipe[]> call = recipesService.getRecipes();
        call.enqueue(new Callback<Recipe[]>() {
            @Override
            public void onResponse(Call<Recipe[]> call, Response<Recipe[]> response) {
                // TODO set list fragment data here
                mRecipeAdapter.setmRecipeData(response.body());
                Timber.d("retrofit success");
                Timber.d("Recipe[] length is " + String.valueOf(response.body().length));
            }

            @Override
            public void onFailure(Call<Recipe[]> call, Throwable t) {
                // TODO display failure here
                Timber.d("retrofit failure");
            }
        });
    }

    @Override
    public void onClick(Recipe clickedRecipe) {
        // TODO handle recipe onclick
        mCallback.onRecipeSelected(clickedRecipe);
        Timber.d("click logging in select recipe fragment");
    }

}
