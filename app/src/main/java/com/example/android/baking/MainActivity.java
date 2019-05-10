package com.example.android.baking;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android.baking.data.Recipe;
import com.example.android.baking.fragments.RecipeListFragment;
import com.example.android.baking.services.GetRecipesService;
import com.example.android.baking.utils.NetworkUtils;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getRecipes();
    }

    private void getRecipes(){
        GetRecipesService recipesService = NetworkUtils.getRetrofitInstance().create(GetRecipesService.class);
        Call<Recipe[]> call = recipesService.getRecipes();
        call.enqueue(new Callback<Recipe[]>() {
            @Override
            public void onResponse(Call<Recipe[]> call, Response<Recipe[]> response) {
                // TODO set list fragment data here
                Log.d("retrofit", "response received");
            }

            @Override
            public void onFailure(Call<Recipe[]> call, Throwable t) {
                // TODO display failure here
                Log.d("retrofit", "call failed: " + t.getMessage());
            }
        });
    }

    /*
    public interface OnRecipeResult {
        void onRecipe
    }
    */
}
