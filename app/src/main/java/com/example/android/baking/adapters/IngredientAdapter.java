package com.example.android.baking.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.baking.R;
import com.example.android.baking.data.Ingredient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder> {

    private Ingredient[] mIngredients;
    private Context mContext;

    public IngredientAdapter(Context context){
        mContext = context;
    }

    public void setData(Ingredient[] ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new IngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientAdapterViewHolder holder, int position) {
        // Update views
        Ingredient ingredient = mIngredients[position];
        holder.mQuantityTv.setText(String.valueOf(ingredient.getQuantity()));
        holder.mMeasureTv.setText(ingredient.getMeasure());
        holder.mIngredientTv.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) return 0;
        else return mIngredients.length;
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.quantity_text_view)
        TextView mQuantityTv;
        @BindView(R.id.measure_text_view)
        TextView mMeasureTv;
        @BindView(R.id.ingredient_text_view)
        TextView mIngredientTv;
        public IngredientAdapterViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
