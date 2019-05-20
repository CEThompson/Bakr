package com.example.android.baking.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.android.baking.RecipeActivity;
import com.example.android.baking.StepsActivity;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.widget.RecipeWidgetProvider;

import java.util.ArrayList;

public class IngredientsWidgetService extends IntentService {

    public static final String ACTION_SAVE_RECIPE_INGREDIENTS ="com.example.android.baking.action.save_ingredients";
    public static final String ACTION_REMOVE_RECIPE_INGREDIENTS ="com.example.android.baking.action.remove_ingredients";
    public static final String ACTION_UPDATE_INGREDIENT_WIDGETS = "com.example.android.baking.action.update_widgets";

    public IngredientsWidgetService(){
        super("IngredientsWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null){
            final String action = intent.getAction();

            if (ACTION_REMOVE_RECIPE_INGREDIENTS.equals(action)){
                handleActionRemoveIngredients();
            }
            else if (ACTION_SAVE_RECIPE_INGREDIENTS.equals(action)){
                // Retrieve the ingredients to be saved
                ArrayList<Ingredient> ingredientList = intent.getParcelableArrayListExtra(StepsActivity.INGREDIENTS_KEY);
                Ingredient[] ingredients = new Ingredient[ingredientList.size()];
                ingredients = ingredientList.toArray(ingredients);

                // Send it to the handler
                handleActionSaveIngredients(ingredients);
            } else if (ACTION_UPDATE_INGREDIENT_WIDGETS.equals(action)){
                handleActionUpdateWidgets();
            }
        }
    }
    public static void startActionSaveIngredients(Context context){
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.setAction(ACTION_SAVE_RECIPE_INGREDIENTS);
        context.startService(intent);
    }
    public static void startActionUpdateIngredientWidgets(Context context){
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGETS);
        context.startService(intent);
    }


    private void handleActionSaveIngredients(Ingredient[] ingredients){

        //TODO save the ingredients to database
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
    }

    private void handleActionRemoveIngredients(){

        // TODO remove ingredients from the database
    }

    private void handleActionUpdateWidgets(){

        // TODO update the widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateIngredientWidgets(this, appWidgetManager, appWidgetIds);
    }


}
