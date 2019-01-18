package com.example.android.bakingappbr;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.example.android.bakingappbr.model.RecipesItem;

public class IngredientWidgetService extends IntentService{
    private static final String BUNDLE_WIDGET_DATA = "widget_data";
    private static final String WIDGET_ACTION="android.appwidget.action.APPWIDGET_UPDATE";

    public IngredientWidgetService() {
        super("IngredientWidgetService");
    }

    public static void startActionWidgets(Context context, RecipesItem recipes) {
        Intent intent = new Intent(context, IngredientWidgetService.class);
        intent.setAction(WIDGET_ACTION);
        intent.putExtra(BUNDLE_WIDGET_DATA, recipes);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (WIDGET_ACTION.equals(action) &&
                intent.getParcelableExtra(BUNDLE_WIDGET_DATA) != null) {


                RecipesItem recipes=(RecipesItem)intent.getParcelableExtra(BUNDLE_WIDGET_DATA);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidget.class));
                IngredientWidget.updateAppWidget(this, appWidgetManager, recipes, appWidgetIds);

            }
        }
    }






}
