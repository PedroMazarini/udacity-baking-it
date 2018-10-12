package com.example.pedro.bakingit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;



/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsWidgetConfigureActivity IngredientsWidgetConfigureActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static final String RECIPE_EXTRA ="recipe_extra" ;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String jsonRecipe = IngredientsWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);

        try {
            Gson gson = new Gson();
            Type recipeType = new TypeToken<Recipe>(){}.getType();
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(jsonRecipe);

            Recipe recipe = gson.fromJson(jsonObject.toString(), recipeType);

            views.setTextViewText(R.id.appwidget_title, recipe.getName());

            String ingredients= "";
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredients = ingredients +" - "+ingredient.getIngredient() + "\n";
            }
            views.setTextViewText(R.id.recipe_ingredients, ingredients);

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra(RECIPE_EXTRA, recipe);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent, 0);
            views.setOnClickPendingIntent(R.id.recipe_ingredients, pendingIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

