package com.example.pantry;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Add / Edit screen for a pantry ingredient.
 * - Opened with no extra  -> "add" mode.
 * - Opened with PantryListActivity.EXTRA_ITEM_ID -> "edit" mode, fields pre-filled from the database.
 */
public class AddIngredientActivity extends AppCompatActivity {

    // Units are limited to these three so recipe matching never has to guess ("grams" vs "g").
    private static final String[] UNITS = {"g", "ml", "pcs"};
    private static final int NO_ITEM = -1;

    private EditText etName, etQuantity, etExpiryDate;
    private Spinner spUnit;
    private Button btnSave;
    private PantryDatabaseHelper dbHelper;
    private int editItemId = NO_ITEM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        dbHelper = new PantryDatabaseHelper(this);

        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        spUnit = findViewById(R.id.spUnit);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        btnSave = findViewById(R.id.btnSaveIngredient);

        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, UNITS);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(unitAdapter);

        // Did PantryListActivity send us an item to edit?
        editItemId = getIntent().getIntExtra(PantryListActivity.EXTRA_ITEM_ID, NO_ITEM);
        if (editItemId != NO_ITEM) {
            loadItemForEditing();
        }

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void loadItemForEditing() {
        setTitle("Edit Ingredient");
        btnSave.setText("Update");

        PantryItem item = dbHelper.getPantryItem(editItemId);
        if (item == null) {
            Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName.setText(item.getName());
        etQuantity.setText(formatQuantity(item.getQuantity()));
        spUnit.setSelection(unitIndex(item.getUnit()));
        if (item.getExpiryDate() != null) {
            etExpiryDate.setText(item.getExpiryDate());
        }
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = (String) spUnit.getSelectedItem();
        String expiry = etExpiryDate.getText().toString().trim();

        // ---- Input validation ----
        if (name.isEmpty()) {
            etName.setError("Enter an ingredient name");
            etName.requestFocus();
            return;
        }
        if (qtyStr.isEmpty()) {
            etQuantity.setError("Enter a quantity");
            etQuantity.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid number");
            etQuantity.requestFocus();
            return;
        }
        if (!(quantity > 0) || Double.isInfinite(quantity)) {
            etQuantity.setError("Quantity must be greater than 0");
            etQuantity.requestFocus();
            return;
        }

        // Expiry date is optional, but if given it must be a real date in YYYY-MM-DD form
        if (!expiry.isEmpty() && !isValidDate(expiry)) {
            etExpiryDate.setError("Use the format YYYY-MM-DD, e.g. 2026-11-30");
            etExpiryDate.requestFocus();
            return;
        }

        // ---- Save: create or update ----
        boolean success;
        if (editItemId == NO_ITEM) {
            success = dbHelper.addPantryItem(name, quantity, unit, expiry);
        } else {
            success = dbHelper.updatePantryItem(editItemId, name, quantity, unit, expiry);
        }

        if (success) {
            Toast.makeText(this,
                    editItemId == NO_ITEM ? "Ingredient saved!" : "Ingredient updated!",
                    Toast.LENGTH_SHORT).show();
            finish(); // Close this screen and return to PantryListActivity (its onResume reloads the list)
        } else {
            // The only normal cause is the UNIQUE constraint on ingredient_name
            etName.setError("That ingredient is already in your pantry");
            etName.requestFocus();
        }
    }

    private boolean isValidDate(String text) {
        // Regex first: SimpleDateFormat alone accepts things like "2026-1-5" or trailing junk
        if (!text.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        format.setLenient(false); // rejects 2026-02-31
        try {
            format.parse(text);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private int unitIndex(String unit) {
        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equalsIgnoreCase(unit)) {
                return i;
            }
        }
        return 0;
    }

    // Shows 2 instead of 2.0, but keeps 1.5 as 1.5
    private String formatQuantity(double quantity) {
        if (quantity == Math.rint(quantity)) {
            return String.valueOf((long) quantity);
        }
        return String.valueOf(quantity);
    }
}