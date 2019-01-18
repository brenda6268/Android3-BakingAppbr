package com.example.android.bakingappbr;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingappbr.model.IngredientItem;
import com.example.android.bakingappbr.model.RecipesItem;

import static com.google.android.exoplayer2.mediacodec.MediaCodecInfo.TAG;


public class IngredientWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                RecipesItem recipes, int[] appWidgetIds) {


        for (int appWidgetId : appWidgetIds) {
            Intent clickInt = EachRecipeActivity.ERIntent(context, recipes);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickInt,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);
            views.removeAllViews(R.id.widget_ingredient_list);
            views.setTextViewText(R.id.widget_recipe_name, recipes.getName());
            Log.d(TAG,recipes.getName() );
            views.setOnClickPendingIntent(R.id.widget_recipe, pendingIntent);

            //add ingredient list as in the EachRecipeFragment did

            for (IngredientItem ingredient : recipes.getIngredients()) {
                RemoteViews rvIngredient = new RemoteViews(context.getPackageName(),
                        R.layout.widget_list_item);
                rvIngredient.setTextViewText(R.id.widget_ingredient_item,
                        String.valueOf(ingredient.getQuantity()) +
                                String.valueOf(ingredient.getMeasure()) + " " + ingredient.getIngredient());

                views.addView(R.id.widget_ingredient_list, rvIngredient);
            }


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }
}

