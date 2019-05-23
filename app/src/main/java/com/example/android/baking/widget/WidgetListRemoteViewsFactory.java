package com.example.android.baking.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.baking.R;
import com.example.android.baking.RecipeActivity;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.google.gson.Gson;

import timber.log.Timber;

public class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    Ingredient[] mIngredients;

    public WidgetListRemoteViewsFactory(Context applicationContext, Intent intent){
        mContext = applicationContext;
        mIngredients = null;
        Timber.d("creating list remote views factory");
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        // Get the saved ingredients from the shared preferences
        SharedPreferences preferences = mContext.getSharedPreferences("pref", Context.MODE_PRIVATE);
        String recipeJson = preferences.getString(RecipeActivity.RECIPE_KEY, null);

        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(recipeJson, Recipe.class);

        if (recipe != null) mIngredients = recipe.getIngredients();

        Timber.d("ListWidgetService onDataSetChanged() fired");
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredients == null)
            return 0;
        else
            return mIngredients.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        String quantity = String.valueOf(mIngredients[i].getQuantity());
        String measure = mIngredients[i].getMeasure();
        String ingredient = mIngredients[i].getIngredient();

        views.setTextViewText(R.id.widget_quantity, quantity);
        views.setTextViewText(R.id.widget_ingredient, ingredient);
        views.setTextViewText(R.id.widget_measure, measure);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
