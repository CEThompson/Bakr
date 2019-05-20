package com.example.android.baking.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.example.android.baking.RecipeActivity;
import com.example.android.baking.StepsActivity;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;
import com.example.android.baking.fragments.ViewStepFragment;
import com.example.android.baking.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.Arrays;

public class IngredientsWidgetService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIENT_WIDGETS = "com.example.android.baking.action.update_widgets";

    public IngredientsWidgetService(){
        super("IngredientsWidgetService");
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
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.setAction(ACTION_UPDATE_INGREDIENT_WIDGETS);
        context.startService(intent);
    }


    private void handleActionUpdateWidgets(){

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);
        String ingredients = preferences.getString(StepsActivity.INGREDIENTS_KEY, null);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider
                .updateIngredientWidgets(
                        this,
                        appWidgetManager,
                        appWidgetIds,
                        ingredients);
    }


}
