package com.example.android.baking.data;

import com.google.gson.annotations.SerializedName;

public class Recipe {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    private Ingredient[] ingredients;

    @SerializedName("steps")
    private Step[] steps;

    @SerializedName("servings")
    private int servings;

    @SerializedName("image")
    private String image;

}
