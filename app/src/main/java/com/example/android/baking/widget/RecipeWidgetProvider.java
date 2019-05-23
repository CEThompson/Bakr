package com.example.android.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;

import com.example.android.baking.R;
import com.example.android.baking.RecipeActivity;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.services.UpdateWidgetService;
import com.example.android.baking.services.WidgetListRemoteViewsService;

import timber.log.Timber;

public class RecipeWidgetProvider extends AppWidgetProvider {

    /* Updates an app widget using its id and relevant info */
    static void updateAppWidget
            (Context context,
             AppWidgetManager appWidgetManager,
             int appWidgetId, @Nullable Recipe recipe){

        RemoteViews views;

        if (recipe == null){
            views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.save_a_recipe));
            views.setViewVisibility(R.id.widget_list_view, View.INVISIBLE);
        } else {
            views = getListRemoteView(context, recipe);
            views.setViewVisibility(R.id.widget_list_view, View.VISIBLE);
            views.setImageViewResource(R.id.muffin_image, R.drawable.icons8_confectionery_48);
            views.setViewVisibility(R.id.widget_add_image, View.INVISIBLE);
        }


        // Set listener to start app on clicking the widget layout
        Intent intent = new Intent(context, RecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        UpdateWidgetService.startActionUpdateIngredientWidgets(context);
    }

    /* Iterates through the list of all the widgets and updates them */
    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetmanager,
                                               int[] appWidgetIds, @Nullable Recipe recipe){
        for (int appWidgetId : appWidgetIds){
            updateAppWidget(context ,appWidgetmanager, appWidgetId, recipe);
        }
        //appWidgetmanager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);

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

    private static RemoteViews getListRemoteView(Context context, Recipe recipe){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);

        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());

        Timber.d("Setting the remote adapter");
        Intent adapterIntent = new Intent(context, WidgetListRemoteViewsService.class);
        views.setRemoteAdapter(R.id.widget_list_view, adapterIntent);

        //Timber.d("inspecting adapterIntent",adapterIntent.);
        //views.setEmptyView(R.id.widget_list_view);

        return views;
    }



}
