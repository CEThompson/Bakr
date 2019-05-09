package com.example.android.baking.fragments;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.recipe_recyclerview) RecyclerView mRecipeRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // TODO bind adapter to recipes
        ButterKnife.bind(view);

        // TODO set up layout manager
        //RecipeAdapter recipeAdapter = new RecipeAdapter(this);
        //mRecipeRecyclerView.setAdapter(recipeAdapter);

        return view;
    }

    @Override
    public void onClick(Recipe clickedRecipe) {
        // TODO handle recipe onclick
    }

}
