package com.example.android.baking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;
import com.example.android.baking.data.Recipe;
import com.example.android.baking.data.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private Recipe[] mRecipeData;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler{
        void onClick(Recipe clickedRecipe);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler handler){ mClickHandler = handler; }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements
    View.OnClickListener{
        @BindView(R.id.recipe_name) TextView mName;

        public RecipeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Recipe clickedRecipe = mRecipeData[position];
            mClickHandler.onClick(clickedRecipe);
            Timber.d("clicking position " + position);
        }
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.recipe_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        Recipe currentRecipe = mRecipeData[position];

        int id = currentRecipe.getId();
        String name = currentRecipe.getName();
        Ingredient[] ingredients = currentRecipe.getIngredients();
        Step[] steps = currentRecipe.getSteps();
        int servings = currentRecipe.getServings();
        String image = currentRecipe.getImage();

        String information = "ID: " + String.valueOf(id) + " "
                + "Name: " + name + " "
                + "Servings: " + servings + " "
                + "Image: " + image;
        holder.mName.setText(information);
    }

    @Override
    public int getItemCount() {
        if (mRecipeData==null)return 0;
        return mRecipeData.length;
    }

    public void setmRecipeData(Recipe[] recipes){
        mRecipeData = recipes;
        notifyDataSetChanged();
        Timber.d("Setting recipe data");
    }
}
