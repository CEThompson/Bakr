package com.example.android.baking.services;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRecipesService {

    @GET(NetworkUtils.RECIPES_ENDPOINT)
    Call<Recipe[]> getRecipes();

}
