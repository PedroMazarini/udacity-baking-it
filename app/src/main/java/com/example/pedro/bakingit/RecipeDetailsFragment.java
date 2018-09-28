package com.example.pedro.bakingit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsFragment extends Fragment {
    private static final String RECIPE_EXTRA ="recipe_extra" ;
    private static final String STEP_EXTRA = "step_extra";
    private static final String STEP_INDEX = "step_index";
    @BindView(R.id.recycler_ingredients)
    RecyclerView recyclerIngredients;
    @BindView(R.id.recycler_steps)
    RecyclerView recyclerSteps;

    IngredientAdapter ingredientAdapter;
    StepAdapter stepAdapter;
    Recipe recipe;

    private OnFragmentInteractionListener mListener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle bundle) {
        View view =inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);
        ingredientAdapter = new IngredientAdapter(this);
        stepAdapter = new StepAdapter(this);

        recyclerIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerIngredients.setAdapter(ingredientAdapter);
        recyclerSteps.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSteps.setAdapter(stepAdapter);
        recipe = getActivity().getIntent().getExtras().getParcelable(RECIPE_EXTRA);

        ingredientAdapter.setList(recipe.getIngredients());
        stepAdapter.setList(recipe.getSteps());
        return view;
    }

    public void callStepDetails(Step step, int index) {
        mListener.onStepClicked(recipe, step, index);
        stepAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void changeListPosition(int index) {
        stepAdapter.changeListPosition(index);
    }

    public interface OnFragmentInteractionListener {
        void onStepClicked(Recipe recipe, Step step, int index);
    }
}
