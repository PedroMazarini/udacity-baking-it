package com.example.pedro.bakingit;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private static final String PREFS_NAME = "com.example.pedro.bakingit.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private static final int FETCH_LOADER = 221;
    private static final String RECIPE_EXTRA ="recipe_extra" ;
    @BindView(R.id.recycler_recipes)
    RecyclerView recyclerRecipes;

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    String recipesJSON = "";
    RecipeAdapter recipeAdapter;

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String jsonRecipe) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, jsonRecipe);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.ingredients_widget_configure);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        ButterKnife.bind(this);
        if(isLargerThan600()){
            recyclerRecipes.setLayoutManager(new GridLayoutManager(this,3));
        }else{
            recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        }
        recipeAdapter = new RecipeAdapter(this);
        recyclerRecipes.setAdapter(recipeAdapter);
        loadRecipes();
    }

    private boolean isLargerThan600() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        float smallestWidth = Math.min(widthDp, heightDp);

        if (smallestWidth > 600) {
            return true;
        }
        return false;
    }

    private void loadRecipes() {
        recipeAdapter.clear();
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> fetchLoader = loaderManager.getLoader(FETCH_LOADER);
        if (fetchLoader == null) {
            loaderManager.initLoader(FETCH_LOADER, new Bundle(), this);
        } else {
            loaderManager.restartLoader(FETCH_LOADER, new Bundle(), this);
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String resultJSON;
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                if (resultJSON != null) {
                    deliverResult(resultJSON);
                } else {
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String loadInBackground() {
                try {
                    URL requestRecipesURL = new URL(Uri.parse(BASE_URL).buildUpon().build().toString());
                    String moviesJSON = getResponseFromHttpUrl(requestRecipesURL);
                    return moviesJSON;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void deliverResult(String resultJSON) {
                recipesJSON = resultJSON;
                super.deliverResult(resultJSON);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String resultJSON) {
        if (resultJSON != null) {
            try {
                JSONArray result = new JSONArray(resultJSON);
                Gson gson = new Gson();
                Type recipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
                List<Recipe> recipeList = gson.fromJson(result.toString(), recipeListType);
                recipeAdapter.setList(recipeList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getApplicationContext(), R.string.failed_load,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public void callRecipeDetails(Recipe recipe) {
        final Context context = IngredientsWidgetConfigureActivity.this;

        Gson gson = new Gson();
        String jsonRecipe = gson.toJson(recipe);

        saveTitlePref(context, mAppWidgetId, jsonRecipe);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        IngredientsWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

