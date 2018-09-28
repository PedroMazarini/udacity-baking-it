package com.example.pedro.bakingit;

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

public class StepAdapter extends  RecyclerView.Adapter<StepAdapter.StepViewHolder>  {

    List<Step> stepList;
    RecipeDetailsFragment recipeDetailsFragment;
    int selected = 0;
    public StepAdapter(RecipeDetailsFragment recipeDetailsFragment) {
        this.recipeDetailsFragment = recipeDetailsFragment;
        stepList = new ArrayList<>();
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(recipeDetailsFragment.getContext())
                .inflate(R.layout.item_step, viewGroup, false);
        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepViewHolder h, final int i) {
        final Step step = stepList.get(i);
        h.txtStepCount.setText(recipeDetailsFragment.getContext().getString(R.string.step_)+" "+String.valueOf((i+1))+":");
        h.txtStep.setText(step.getDescription());
        if(i==selected) {
            h.stepLayout.setBackground(recipeDetailsFragment.getContext().getDrawable(R.drawable.title_background));
            h.stepLayout.setBackgroundColor(recipeDetailsFragment.getContext().getColor(R.color.white));
            h.txtStep.setTextColor(recipeDetailsFragment.getContext().getColor(R.color.darkBlue));
            h.txtStepCount.setTextColor(recipeDetailsFragment.getContext().getColor(R.color.darkBlue));
            h.stepSeparator.setVisibility(View.GONE);
        }else {
            h.stepLayout.setBackgroundColor(recipeDetailsFragment.getContext().getColor(R.color.lightBlue));
            h.txtStep.setTextColor(recipeDetailsFragment.getContext().getColor(R.color.white));
            h.txtStepCount.setTextColor(recipeDetailsFragment.getContext().getColor(R.color.white));
            h.stepSeparator.setVisibility(View.VISIBLE);
        }

        h.stepLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeDetailsFragment.callStepDetails(step, i);
                selected = i;
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    public void clear(){
        stepList.clear();
        this.notifyDataSetChanged();
    }


    public void setList(List<Step> stepList){
        this.stepList = stepList;
        this.notifyDataSetChanged();
    }

    public void changeListPosition(int index) {
        this.selected = index;
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_separator)
        View stepSeparator;
        @BindView(R.id.step_item_layout)
        RelativeLayout stepLayout;
        @BindView(R.id.txt_step_count)
        TextView txtStepCount;
        @BindView(R.id.txt_step_description)
        TextView txtStep;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
