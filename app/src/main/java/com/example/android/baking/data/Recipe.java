package com.example.android.baking.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Recipe implements Parcelable {

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

    // Empty constructor
    public Recipe(){}

    // Getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Ingredient[] getIngredients() {
        return ingredients;
    }
    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }
    public Step[] getSteps() {
        return steps;
    }
    public void setSteps(Step[] steps) {
        this.steps = steps;
    }
    public int getServings() {
        return servings;
    }
    public void setServings(int servings) {
        this.servings = servings;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedArray(ingredients, 0);
        parcel.writeTypedArray(steps, 0);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) { return new Recipe(in);}
        public Recipe[] newArray(int size) { return new Recipe[size]; }

    };

    private Recipe(Parcel in){
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArray(Ingredient.CREATOR);
        this.steps = in.createTypedArray(Step.CREATOR);
        this.servings = in.readInt();
        this.image = in.readString();
    }

}

