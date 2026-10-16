package com.example.pantry;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity {

    private static final String[] UNITS = {"g", "ml", "pcs"};

    private EditText etRecipeName, etInstructions, etIngName, etIngQty;
    private Spinner spIngUnit;
    private TextView tvAddedIngredientsList;
    private PantryDatabaseHelper dbHelper;
    private final List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        setTitle("Add Custom Recipe");

        dbHelper = new PantryDatabaseHelper(this);

        etRecipeName = findViewById(R.id.etRecipeName);
        etInstructions = findViewById(R.id.etInstructions);
        etIngName = findViewById(R.id.etIngName);
        etIngQty = findViewById(R.id.etIngQty);
        spIngUnit = findViewById(R.id.spIngUnit);
        tvAddedIngredientsList = findViewById(R.id.tvAddedIngredientsList);
        Button btnAddIngToList = findViewById(R.id.btnAddIngToList);
        Button btnSaveRecipe = findViewById(R.id.btnSaveRecipe);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UNITS);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIngUnit.setAdapter(unitAdapter);

        btnAddIngToList.setOnClickListener(v -> addIngredientToLocalList());
        btnSaveRecipe.setOnClickListener(v -> saveRecipeToDatabase());
    }

    private void addIngredientToLocalList() {
        String name = etIngName.getText().toString().trim();
        String qtyStr = etIngQty.getText().toString().trim();
        String unit = (String) spIngUnit.getSelectedItem();

        if (name.isEmpty()) {
            etIngName.setError("Enter ingredient name");
            return;
        }
        if (qtyStr.isEmpty()) {
            etIngQty.setError("Enter quantity");
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            etIngQty.setError("Invalid number");
            return;
        }

        recipeIngredients.add(new RecipeIngredient(name, qty, unit));
        updateIngredientsDisplay();

        etIngName.setText("");
        etIngQty.setText("");
        etIngName.requestFocus();
    }

    private void updateIngredientsDisplay() {
        if (recipeIngredients.isEmpty()) {
            tvAddedIngredientsList.setText("No ingredients added yet.");
            return;
        }
        StringBuilder sb = new StringBuilder("Added Ingredients:\n");
        for (RecipeIngredient ri : recipeIngredients) {
            sb.append("• ").append(ri.getName()).append(" - ").append(ri.getQuantity()).append(" ").append(ri.getUnit()).append("\n");
        }
        tvAddedIngredientsList.setText(sb.toString().trim());
    }

    private void saveRecipeToDatabase() {
        String name = etRecipeName.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();

        if (name.isEmpty()) {
            etRecipeName.setError("Enter a recipe name");
            etRecipeName.requestFocus();
            return;
        }
        if (instructions.isEmpty()) {
            etInstructions.setError("Enter instructions");
            etInstructions.requestFocus();
            return;
        }
        if (recipeIngredients.isEmpty()) {
            Toast.makeText(this, "Add at least one ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = dbHelper.addRecipe(name, instructions, recipeIngredients);
        if (id != -1) {
            Toast.makeText(this, "Recipe saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving recipe", Toast.LENGTH_SHORT).show();
        }
    }
}