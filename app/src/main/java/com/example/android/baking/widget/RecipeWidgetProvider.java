package com.example.android.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.baking.R;
import com.example.android.baking.RecipeActivity;
import com.example.android.baking.services.IngredientsWidgetService;

public class RecipeWidgetProvider extends AppWidgetProvider {
    // TODO implement widget

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        views.setTextViewText(R.id.widget_text, "placeholder text");

        // Set listener to start app on widget click
        Intent intent = new Intent(context, RecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.muffin_image, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        IngredientsWidgetService.startActionUpdateIngredientWidgets(context);

    }

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetmanager,
                                               int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context ,appWidgetmanager, appWidgetId);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }
}
