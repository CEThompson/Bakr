package com.example.android.baking.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.android.baking.RecipeActivity;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.widget.RecipeWidgetProvider;
import com.google.gson.Gson;

import java.util.Arrays;

import timber.log.Timber;

public class UpdateWidgetService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENT_WIDGETS = "com.example.android.baking.action.update_widgets";

    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null){
            final String action = intent.getAction();

            if (ACTION_UPDATE_INGREDIENT_WIDGETS.equals(action)){
                handleActionUpdateWidgets();
            }
        }
    }

    public static void startActionUpdateIngredientWidgets(Context context){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGETS);
        context.startService(intent);
    }


    private void handleActionUpdateWidgets(){

        // Get the saved ingredients from the shared preferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String recipeJson = preferences.getString(RecipeActivity.RECIPE_KEY, null);

        Gson gson = new Gson();
        Recipe recipe = null;

        if (recipeJson != null) {
            recipe = gson.fromJson(recipeJson, Recipe.class);

            Timber.d("checking shared pref recipe, recipe is %s", recipe.getName());
            Ingredient[] ingredients = recipe.getIngredients();
            for (Ingredient ingredient : ingredients ){
                Timber.d("Ingredient is %s", ingredient.getIngredient());
            }
        } else {
            Timber.d("checking shared pref recipe, recipe is null");
        }

        // Update widgets with this string
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateIngredientWidgets(
                        this,
                        appWidgetManager,
                        appWidgetIds,
                        recipe);
    }


}
