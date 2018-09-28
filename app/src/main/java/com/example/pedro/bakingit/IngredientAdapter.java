package com.example.pedro.bakingit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends  RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>  {

    List<Ingredient> ingredients;
    RecipeDetailsFragment recipeDetailsFragment;

    public IngredientAdapter(RecipeDetailsFragment recipeDetailsFragment) {
        this.recipeDetailsFragment = recipeDetailsFragment;
        ingredients = new ArrayList<>();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(recipeDetailsFragment.getContext())
                .inflate(R.layout.item_ingredient, viewGroup, false);
        return new IngredientAdapter.IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder h, int i) {
        final Ingredient ingredient = ingredients.get(i);
        h.txtIngredient.setText(ingredient.getIngredient());
        h.txtQuantity.setText(String.valueOf(ingredient.getQuantity())+ " "+ ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void clear(){
        ingredients.clear();
        this.notifyDataSetChanged();
    }


    public void setList(List<Ingredient> ingredients){
        this.ingredients = ingredients;
        this.notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_quantity)
        TextView txtQuantity;
        @BindView(R.id.txt_ingredient)
        TextView txtIngredient;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
