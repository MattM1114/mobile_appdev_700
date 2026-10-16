package com.example.pantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView rvRecipes;
    private TextView tvEmptyRecipes;
    private PantryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        setTitle("Suggested Recipes");

        rvRecipes = findViewById(R.id.rvRecipes);
        tvEmptyRecipes = findViewById(R.id.tvEmptyRecipes);
        dbHelper = new PantryDatabaseHelper(this);

        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {
        // Fetches recipes that can be made with current pantry items
        List<Recipe> recipes = dbHelper.getSuggestedRecipes();

        if (recipes.isEmpty()) {
            tvEmptyRecipes.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            tvEmptyRecipes.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);

            RecipeAdapter adapter = new RecipeAdapter(recipes, recipe -> {
                Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
                startActivity(intent);
            });
            rvRecipes.setAdapter(adapter);
        }
    }
}