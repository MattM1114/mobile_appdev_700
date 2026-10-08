package com.example.pantry;

// setup imports
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pantry.db";
    private static final int DATABASE_VERSION = 1;

    //1. PANTRY TABLE

    public static final String TABLE_PANTRY = "pantry";
    public static final String COL_PANTRY_ID = "id";

    public static final String COL_INGREDIENT_NAME = "ingredient_name";
    public static final String COL_QUANTITY ="quantity";
    public static final String COL_UNIT ="UNIT";

    // 2 Recipes Table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPES_ID = "id";
    public static final String COL_RECIPES_NAME = "recipe_name";

    public static final String COL_INSTRUCTIONS = "instructions";

    // 3 recipe ingredients junction Table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_MAP_ID ="id";
    public static final String COL_MAP_RECIPE_ID = "recipe_id";

    public static final String COL_MAP_INGREDIENT_NAME = "ingredint_name";

    public PantryDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create Pantry Table
        String createPantry = "CREATE TABLE "+TABLE_PANTRY +" ("+COL_PANTRY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_INGREDIENT_NAME + " TEXT UNIQUE, "+ COL_QUANTITY + " REAL, "+COL_UNIT+" TEXT);";
        // Create Recipes Table
        String createRecipes = "CREATE TABLE "+ TABLE_RECIPES +" ("+COL_RECIPES_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
        COL_RECIPES_NAME+ " TEXT, "+ COL_INSTRUCTIONS + " TEXT);";
        // Create Recipe ingredients table
        String createRecipeIngredients = "CREATE TABLE "+ TABLE_RECIPE_INGREDIENTS +" ("+ COL_MAP_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COL_MAP_RECIPE_ID + " INTEGER, "+ COL_MAP_INGREDIENT_NAME + " TEXT);";

        db.execSQL(createPantry);
        db.execSQL(createRecipes);
        db.execSQL(createRecipeIngredients);

        // Pre-seed sample recipes into database
        seedInitialRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_RECIPES);
        db.execSQL(("DROP TABLE IF EXISTS "+ TABLE_RECIPE_INGREDIENTS));
        onCreate(db);
    }

    // Insert sample recipe data for testing the strict matching algorithm
    private void seedInitialRecipes(SQLiteDatabase db){
        // Recipe 1: omelette
        ContentValues recipe1 = new ContentValues();
        recipe1.put(COL_RECIPES_NAME,"Simple Omelette");
        recipe1.put(COL_INSTRUCTIONS,"Beat the eggs,pour into hot pan, fold and serve.");
        long r1Id= db.insert(TABLE_RECIPES,null,recipe1);

        addRecipeIngredient(db,r1Id, "Eggs");
        addRecipeIngredient(db,r1Id, "Milk");

        // Recipe 2 pancakes
        ContentValues recipe2 = new ContentValues();
        recipe2.put(COL_RECIPES_NAME,"Fluffy pancakes");
        recipe2.put(COL_INSTRUCTIONS,"Mix flour, eggs, and milk. Fry on griddle.");
        long r2Id = db.insert(TABLE_RECIPES,null,recipe2);

        addRecipeIngredient(db,r2Id,"Flour");
        addRecipeIngredient(db,r2Id,"Eggs");
        addRecipeIngredient(db,r2Id,"Milk");
    }
    private void addRecipeIngredient(SQLiteDatabase db, long recipeId, String ingredient){
        ContentValues val = new ContentValues();
        val.put(COL_MAP_RECIPE_ID,recipeId);
        val.put(COL_MAP_INGREDIENT_NAME,ingredient);

        db.insert(TABLE_RECIPE_INGREDIENTS,null,val);
    }
    // Helper: Add item to pantry
    public boolean addPantryItem(String name, double qty, String unit){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL_INGREDIENT_NAME,name);
        cv.put(COL_QUANTITY, qty);
        cv.put(COL_UNIT,unit);
        long res = db.insert(TABLE_PANTRY,null,cv);

        return res != -1;
    }


}
