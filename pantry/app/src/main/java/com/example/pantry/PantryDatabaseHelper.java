package com.example.pantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PantryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 3;

    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY_DATE = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPES_ID = "id";
    public static final String COL_RECIPES_NAME = "recipe_name";
    public static final String COL_INSTRUCTIONS = "instructions";

    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_MAP_ID = "id";
    public static final String COL_MAP_RECIPE_ID = "recipe_id";
    public static final String COL_MAP_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_MAP_QUANTITY = "quantity";
    public static final String COL_MAP_UNIT = "unit";

    public PantryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " ("
                + COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_INGREDIENT_NAME + " TEXT NOT NULL UNIQUE COLLATE NOCASE, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT, "
                + COL_EXPIRY_DATE + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " ("
                + COL_RECIPES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_RECIPES_NAME + " TEXT NOT NULL, "
                + COL_INSTRUCTIONS + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " ("
                + COL_MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_MAP_RECIPE_ID + " INTEGER NOT NULL, "
                + COL_MAP_INGREDIENT_NAME + " TEXT NOT NULL, "
                + COL_MAP_QUANTITY + " REAL NOT NULL, "
                + COL_MAP_UNIT + " TEXT, "
                + "FOREIGN KEY(" + COL_MAP_RECIPE_ID + ") REFERENCES "
                + TABLE_RECIPES + "(" + COL_RECIPES_ID + ") ON DELETE CASCADE);");

        seedInitialRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    private void seedInitialRecipes(SQLiteDatabase db) {
        seedRecipe(db, "Simple Omelette",
                "Beat the eggs with the milk, pour into a hot pan, fold and serve.",
                "egg:2:pcs", "milk:30:ml");
        seedRecipe(db, "Fluffy Pancakes",
                "Mix flour, egg and milk into a batter. Fry spoonfuls on a hot griddle until golden.",
                "flour:150:g", "egg:1:pcs", "milk:200:ml");
        seedRecipe(db, "Scrambled Eggs on Toast",
                "Toast the bread. Melt butter in a pan, add beaten eggs and stir gently until just set. Serve on the toast.",
                "egg:3:pcs", "bread:2:pcs", "butter:10:g");
        seedRecipe(db, "Cheese Toastie",
                "Butter the bread, add cheese between the slices and fry or grill until golden and melted.",
                "bread:2:pcs", "cheese:40:g", "butter:10:g");
        seedRecipe(db, "Tomato Pasta",
                "Boil the pasta. Fry garlic in olive oil, add chopped tomatoes and simmer, then toss with the pasta.",
                "pasta:100:g", "tomato:3:pcs", "garlic:1:pcs", "olive oil:15:ml");
        seedRecipe(db, "Egg Fried Rice",
                "Fry chopped onion, add cooked rice and soy sauce, push aside and scramble the eggs in, then mix together.",
                "rice:150:g", "egg:2:pcs", "onion:1:pcs", "soy sauce:15:ml");
        seedRecipe(db, "Garlic Bread",
                "Mash crushed garlic into butter, spread on the bread and bake until crisp.",
                "bread:4:pcs", "butter:30:g", "garlic:2:pcs");
        seedRecipe(db, "French Toast",
                "Whisk the eggs and milk, soak the bread slices and fry until golden on both sides.",
                "bread:4:pcs", "egg:2:pcs", "milk:100:ml");
        seedRecipe(db, "Mashed Potatoes",
                "Boil peeled potatoes until soft, drain, then mash with butter and milk.",
                "potato:4:pcs", "butter:30:g", "milk:50:ml");
        seedRecipe(db, "Cheese Omelette",
                "Beat the eggs, cook in buttered pan, add grated cheese, fold and serve.",
                "egg:3:pcs", "cheese:50:g", "butter:10:g");
        seedRecipe(db, "Simple Tomato Soup",
                "Fry chopped onion in butter, add chopped tomatoes and simmer for 20 minutes, then blend until smooth.",
                "tomato:5:pcs", "onion:1:pcs", "butter:20:g");
        seedRecipe(db, "Banana Pancakes",
                "Mash the bananas, mix in eggs and flour, and fry small pancakes until golden.",
                "banana:2:pcs", "egg:2:pcs", "flour:100:g");
        seedRecipe(db, "Quick Carbonara",
                "Boil the pasta. Fry the bacon, then toss with the hot pasta, beaten eggs and grated cheese off the heat.",
                "pasta:200:g", "egg:2:pcs", "cheese:50:g", "bacon:100:g");
        seedRecipe(db, "Chicken and Rice",
                "Brown the chicken pieces with onion, add rice and water, cover and cook until the rice is tender.",
                "chicken:300:g", "rice:150:g", "onion:1:pcs");
        seedRecipe(db, "Oat Porridge",
                "Simmer the oats in milk, stirring, until thick. Drizzle with honey.",
                "oats:50:g", "milk:250:ml", "honey:10:ml");
        seedRecipe(db, "Cheesy Baked Potato",
                "Bake the potatoes until soft, split open and fill with butter and grated cheese.",
                "potato:2:pcs", "cheese:50:g", "butter:10:g");
        seedRecipe(db, "Peanut Butter Sandwich",
                "Spread peanut butter on one slice of bread and top with the other.",
                "bread:2:pcs", "peanut butter:30:g");
        seedRecipe(db, "Macaroni Cheese",
                "Boil the pasta. Melt butter, add milk and cheese to make a sauce, then stir in the pasta.",
                "pasta:200:g", "cheese:80:g", "milk:200:ml", "butter:20:g");
    }

    private void seedRecipe(SQLiteDatabase db, String name, String instructions, String... ingredients) {
        ContentValues recipe = new ContentValues();
        recipe.put(COL_RECIPES_NAME, name);
        recipe.put(COL_INSTRUCTIONS, instructions);
        long recipeId = db.insert(TABLE_RECIPES, null, recipe);

        for (String line : ingredients) {
            String[] parts = line.split(":");
            ContentValues val = new ContentValues();
            val.put(COL_MAP_RECIPE_ID, recipeId);
            val.put(COL_MAP_INGREDIENT_NAME, parts[0].trim());
            val.put(COL_MAP_QUANTITY, Double.parseDouble(parts[1].trim()));
            val.put(COL_MAP_UNIT, parts[2].trim());
            db.insert(TABLE_RECIPE_INGREDIENTS, null, val);
        }
    }

    // ----- Pantry CRUD -----

    public boolean addPantryItem(String name, double qty, String unit, String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_PANTRY, null, buildPantryValues(name, qty, unit, expiryDate));
        return result != -1;
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, null, null, null, null,
                COL_INGREDIENT_NAME + " ASC");
        try {
            while (cursor.moveToNext()) items.add(cursorToItem(cursor));
        } finally { cursor.close(); }
        return items;
    }

    public PantryItem getPantryItem(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_PANTRY, null, COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (cursor.moveToFirst()) return cursorToItem(cursor);
            return null;
        } finally { cursor.close(); }
    }

    public boolean updatePantryItem(int id, String name, double qty, String unit, String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            int rows = db.update(TABLE_PANTRY, buildPantryValues(name, qty, unit, expiryDate),
                    COL_PANTRY_ID + " = ?", new String[]{String.valueOf(id)});
            return rows > 0;
        } catch (SQLiteConstraintException e) { return false; }
    }

    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_PANTRY_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }
    public long addRecipe(String name, String instructions, List<RecipeIngredient> ingredients) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues recipe = new ContentValues();
        recipe.put(COL_RECIPES_NAME, name.trim());
        recipe.put(COL_INSTRUCTIONS, instructions.trim());
        long recipeId = db.insert(TABLE_RECIPES, null, recipe);

        if (recipeId != -1 && ingredients != null) {
            for (RecipeIngredient ing : ingredients) {
                ContentValues val = new ContentValues();
                val.put(COL_MAP_RECIPE_ID, recipeId);
                val.put(COL_MAP_INGREDIENT_NAME, ing.getName().trim());
                val.put(COL_MAP_QUANTITY, ing.getQuantity());
                val.put(COL_MAP_UNIT, ing.getUnit());
                db.insert(TABLE_RECIPE_INGREDIENTS, null, val);
            }
        }
        return recipeId;
    }


    // ----- Recipe reading -----

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, null, null, null, null,
                COL_RECIPES_NAME + " ASC");
        try {
            while (c.moveToNext()) {
                int id = c.getInt(c.getColumnIndexOrThrow(COL_RECIPES_ID));
                String name = c.getString(c.getColumnIndexOrThrow(COL_RECIPES_NAME));
                String instructions = c.getString(c.getColumnIndexOrThrow(COL_INSTRUCTIONS));
                recipes.add(new Recipe(id, name, instructions, getIngredientsForRecipe(id)));
            }
        } finally { c.close(); }
        return recipes;
    }

    public Recipe getRecipeById(int recipeId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPES, null, COL_RECIPES_ID + " = ?",
                new String[]{String.valueOf(recipeId)}, null, null, null);
        try {
            if (c.moveToFirst()) {
                int id = c.getInt(c.getColumnIndexOrThrow(COL_RECIPES_ID));
                String name = c.getString(c.getColumnIndexOrThrow(COL_RECIPES_NAME));
                String instructions = c.getString(c.getColumnIndexOrThrow(COL_INSTRUCTIONS));
                return new Recipe(id, name, instructions, getIngredientsForRecipe(id));
            }
            return null;
        } finally { c.close(); }
    }

    private List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> out = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_RECIPE_INGREDIENTS, null,
                COL_MAP_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, COL_MAP_ID + " ASC");
        try {
            while (c.moveToNext()) {
                out.add(new RecipeIngredient(
                        c.getString(c.getColumnIndexOrThrow(COL_MAP_INGREDIENT_NAME)),
                        c.getDouble(c.getColumnIndexOrThrow(COL_MAP_QUANTITY)),
                        c.getString(c.getColumnIndexOrThrow(COL_MAP_UNIT))));
            }
        } finally { c.close(); }
        return out;
    }

    // ----- Strict-matching rule (assignment section 2.3) -----

    public List<Recipe> getSuggestedRecipes() {
        Map<String, PantryItem> pantry = buildPantryMap();
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : getAllRecipes()) {
            if (countMissingIngredients(r, pantry) == 0) {
                r.setMissingCount(0);
                result.add(r);
            }
        }
        return result;
    }

    public List<Recipe> getAlmostThereRecipes() {
        Map<String, PantryItem> pantry = buildPantryMap();
        List<Recipe> result = new ArrayList<>();
        for (Recipe r : getAllRecipes()) {
            int missing = countMissingIngredients(r, pantry);
            if (missing == 1) { r.setMissingCount(1); result.add(r); }
        }
        return result;
    }

    private Map<String, PantryItem> buildPantryMap() {
        Map<String, PantryItem> map = new HashMap<>();
        for (PantryItem item : getAllPantryItems())
            map.put(normalizeName(item.getName()), item);
        return map;
    }

    private int countMissingIngredients(Recipe recipe, Map<String, PantryItem> pantry) {
        int missing = 0;
        for (RecipeIngredient req : recipe.getIngredients()) {
            PantryItem have = pantry.get(normalizeName(req.getName()));
            if (have == null) { missing++; continue; }
            if (!baseUnit(req.getUnit()).equals(baseUnit(have.getUnit()))) { missing++; continue; }
            double haveQty = toBaseQuantity(have.getQuantity(), have.getUnit());
            double reqQty  = toBaseQuantity(req.getQuantity(),  req.getUnit());
            if (haveQty < reqQty) missing++;
        }
        return missing;
    }

    static String normalizeName(String raw) {
        if (raw == null) return "";
        String n = raw.trim().toLowerCase(Locale.US).replaceAll("\\s+", " ");
        if (n.endsWith("ies") && n.length() > 4) {
            n = n.substring(0, n.length() - 3) + "y";
        } else if (n.endsWith("oes") && n.length() > 4) {
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("ches") || n.endsWith("shes")
                || n.endsWith("xes") || n.endsWith("zes") || n.endsWith("ses")) {
            n = n.substring(0, n.length() - 2);
        } else if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 3) {
            n = n.substring(0, n.length() - 1);
        }
        return n;
    }

    private static String baseUnit(String unit) {
        if (unit == null) return "";
        String u = unit.trim().toLowerCase(Locale.US);
        switch (u) {
            case "kg": case "g":                                 return "g";
            case "l": case "litre": case "liter": case "ml":     return "ml";
            case "pc": case "pcs": case "piece": case "pieces":  return "pcs";
            default: return u;
        }
    }

    private static double toBaseQuantity(double qty, String unit) {
        if (unit == null) return qty;
        String u = unit.trim().toLowerCase(Locale.US);
        if (u.equals("kg") || u.equals("l") || u.equals("litre") || u.equals("liter"))
            return qty * 1000.0;
        return qty;
    }

    private ContentValues buildPantryValues(String name, double qty, String unit, String expiryDate) {
        ContentValues cv = new ContentValues();
        cv.put(COL_INGREDIENT_NAME, name.trim());
        cv.put(COL_QUANTITY, qty);
        cv.put(COL_UNIT, unit);
        if (expiryDate == null || expiryDate.trim().isEmpty()) cv.putNull(COL_EXPIRY_DATE);
        else cv.put(COL_EXPIRY_DATE, expiryDate.trim());
        return cv;
    }

    private PantryItem cursorToItem(Cursor c) {
        int expiryIndex = c.getColumnIndexOrThrow(COL_EXPIRY_DATE);
        String expiry = c.isNull(expiryIndex) ? null : c.getString(expiryIndex);
        return new PantryItem(
                c.getInt(c.getColumnIndexOrThrow(COL_PANTRY_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_INGREDIENT_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(COL_QUANTITY)),
                c.getString(c.getColumnIndexOrThrow(COL_UNIT)),
                expiry);
    }

}