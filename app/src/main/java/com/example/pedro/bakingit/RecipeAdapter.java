package com.example.pedro.bakingit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends  RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>  {

    List<Recipe> recipeList;
    MainActivity mainActivity;
    IngredientsWidgetConfigureActivity ingredientsWidgetConfigureActivity;

    public RecipeAdapter(Activity activity) {
        if(activity  instanceof MainActivity)
            this.mainActivity = (MainActivity) activity;
        else
            this.ingredientsWidgetConfigureActivity = (IngredientsWidgetConfigureActivity) activity;
        recipeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        if(mainActivity != null)
            itemView = LayoutInflater.from(mainActivity)
                    .inflate(R.layout.item_recipe, viewGroup, false);
        else
            itemView = LayoutInflater.from(ingredientsWidgetConfigureActivity)
                .inflate(R.layout.item_recipe, viewGroup, false);
        return new RecipeAdapter.RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder h, int i) {
        final Recipe recipe = recipeList.get(i);
        h.txtRecipeName.setText(recipe.getName());
        h.txtServes.setText(String.valueOf(recipe.getServings()));
        h.recipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mainActivity!=null)
                    mainActivity.callRecipeDetails(recipe);
                else
                    ingredientsWidgetConfigureActivity.callRecipeDetails(recipe);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public void clear(){
        recipeList.clear();
        this.notifyDataSetChanged();
    }


    public void setList(List<Recipe> recipeList){
        this.recipeList = recipeList;
        this.notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_recipe_name)
        TextView txtRecipeName;
        @BindView(R.id.txt_serves_number)
        TextView txtServes;
        @BindView(R.id.recipe_item_layout)
        RelativeLayout recipeLayout;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
