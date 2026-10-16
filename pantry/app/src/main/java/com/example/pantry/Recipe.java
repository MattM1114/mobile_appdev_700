package com.example.pantry;

import java.util.List;

public class Recipe {
    private final int id;
    private final String name;
    private final String instructions;
    private final List<RecipeIngredient> ingredients;
    private int missingCount;

    public Recipe(int id, String name, String instructions, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.missingCount = 0;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }

    public int getMissingCount() { return missingCount; }
    public void setMissingCount(int missingCount) { this.missingCount = missingCount; }
}