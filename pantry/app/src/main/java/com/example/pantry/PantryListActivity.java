package com.example.pantry;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {

    // Intent extra used to tell AddIngredientActivity which item to edit.
    // If the extra is missing (-1), AddIngredientActivity is in "add" mode.
    public static final String EXTRA_ITEM_ID = "com.example.smartpantrymanager.EXTRA_ITEM_ID";

    private RecyclerView rvPantryList;
    private TextView tvEmptyPantry;
    private Button btnAddIngredient, btnFindRecipes;
    private PantryDatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        // Initialize UI components
        rvPantryList = findViewById(R.id.rvPantryList);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnFindRecipes = findViewById(R.id.btnFindRecipes);

        dbHelper = new PantryDatabaseHelper(this);
        pantryList = new ArrayList<>();

        // Setup RecyclerView
        rvPantryList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(pantryList, new PantryAdapter.OnItemActionListener() {
            @Override
            public void onEditClick(PantryItem item) {
                Intent intent = new Intent(PantryListActivity.this, AddIngredientActivity.class);
                intent.putExtra(EXTRA_ITEM_ID, item.getId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(PantryItem item) {
                confirmDelete(item);
            }
        });
        rvPantryList.setAdapter(adapter);

        // Add Ingredient button -> AddIngredientActivity (add mode, no extra)
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantryListActivity.this, AddIngredientActivity.class);
                startActivity(intent);
            }
        });

        // Find Recipes button -> RecipeListActivity
        btnFindRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantryListActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data whenever the activity comes back to the foreground
        loadPantryData();
    }

    private void loadPantryData() {
        pantryList.clear();
        try {
            pantryList.addAll(dbHelper.getAllPantryItems());
        } catch (Exception e) {
            Toast.makeText(this, "Error loading pantry data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // Notify adapter of data changes
        adapter.notifyDataSetChanged();

        // Toggle visibility between empty view and RecyclerView
        if (pantryList.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            rvPantryList.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            rvPantryList.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete(final PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Remove " + item.getName() + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (dbHelper.deletePantryItem(item.getId())) {
                        Toast.makeText(this, item.getName() + " deleted", Toast.LENGTH_SHORT).show();
                        loadPantryData();
                    } else {
                        Toast.makeText(this, "Could not delete item", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}