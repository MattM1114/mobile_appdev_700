package com.example.smartpantrymanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class PantryListActivity extends AppCompatActivity {

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
        adapter = new PantryAdapter(this, pantryList);
        rvPantryList.setAdapter(adapter);

        // Handle Add Ingredient Button Click
        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PantryListActivity.this, AddIngredientActivity.class);
                startActivity(intent);
            }
        });

        // Handle Find Recipes Button Click
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
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query your SQLite database table
            cursor = db.rawQuery("SELECT * FROM pantry_items", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                    String expiryDate = cursor.getString(cursor.getColumnIndexOrThrow("expiry_date"));

                    // Add item to list
                    pantryList.add(new PantryItem(id, name, quantity, expiryDate));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading pantry data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
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
}