package com.example.pantry;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeDetailActivity extends AppCompatActivity {

    // Intent extra holding the id of the recipe to show. The value matches the "RECIPE_ID"
    // string used before, so a list screen that already sends it keeps working.
    public static final String EXTRA_RECIPE_ID = "RECIPE_ID";

    private TextView tvTitle, tvIngredients, tvInstructions;
    private PantryDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        dbHelper = new PantryDatabaseHelper(this);

        tvTitle = findViewById(R.id.tvRecipeDetailTitle);
        tvIngredients = findViewById(R.id.tvRecipeDetailIngredients);
        tvInstructions = findViewById(R.id.tvRecipeDetailInstructions);

        // Retrieve the recipe id passed through the Intent
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, -1);
        if (recipeId != -1) {
            loadRecipeDetails(recipeId);
        } else {
            Toast.makeText(this, "Error loading recipe details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadRecipeDetails(int recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] idArg = new String[]{String.valueOf(recipeId)};

        // 1. Fetch recipe title and instructions
        Cursor cursor = db.query(
                PantryDatabaseHelper.TABLE_RECIPES,
                null,
                PantryDatabaseHelper.COL_RECIPES_ID + " = ?",
                idArg,
                null, null, null);

        try {
            if (cursor.moveToFirst()) {
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_RECIPES_NAME));
                String instructions = cursor.getString(
                        cursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_INSTRUCTIONS));

                tvTitle.setText(title);
                tvInstructions.setText(instructions);
                setTitle(title);
            } else {
                Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } finally {
            cursor.close();
        }

        // 2. Fetch the required ingredients (name + quantity + unit)
        Cursor ingCursor = db.query(
                PantryDatabaseHelper.TABLE_RECIPE_INGREDIENTS,
                null,
                PantryDatabaseHelper.COL_MAP_RECIPE_ID + " = ?",
                idArg,
                null, null,
                PantryDatabaseHelper.COL_MAP_ID + " ASC");

        StringBuilder ingBuilder = new StringBuilder();
        try {
            while (ingCursor.moveToNext()) {
                String ingredientName = ingCursor.getString(
                        ingCursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_MAP_INGREDIENT_NAME));
                double quantity = ingCursor.getDouble(
                        ingCursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_MAP_QUANTITY));
                String unit = ingCursor.getString(
                        ingCursor.getColumnIndexOrThrow(PantryDatabaseHelper.COL_MAP_UNIT));

                ingBuilder.append("• ").append(ingredientName)
                        .append(" - ").append(formatQuantity(quantity));
                if (unit != null && !unit.isEmpty()) {
                    ingBuilder.append(" ").append(unit);
                }
                ingBuilder.append("\n");
            }
        } finally {
            ingCursor.close();
        }

        tvIngredients.setText(ingBuilder.toString().trim());
    }

    // Shows 2 instead of 2.0, but keeps 1.5 as 1.5
    private String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}