Here is a comprehensive and professional `README.md` file tailored for your **Smart Pantry Manager** Android application, incorporating your app's architecture, database helper design, and key features.

---

# Smart Pantry Manager

An Android application built in Java designed to help users effortlessly manage their kitchen inventory, track item quantities and expiration dates, and discover or create recipes based on what is currently available in their pantry.

---

## ✨ Key Features

* **Pantry Inventory Management**: Add, update, and delete ingredients with robust validation for names, quantities, and units (`g`, `ml`, `pcs`).
* **Expiration Date Tracking**: Optional expiration date input with strict formatting and parsing rules (`YYYY-MM-DD`).
* **Smart Recipe Suggestions**: Automatically filters and suggests recipes based on exact pantry inventory matches and unit conversions.
* **Custom Recipe Creator**: Dynamically create and save your own custom recipes along with their required ingredients directly to the database.
* **SQLite Database Integration**: Built using `SQLiteOpenHelper` to handle local data storage, foreign key constraints, and initial recipe seeding.

---

## 🛠️ Tech Stack & Architecture

* **Language**: Java
* **Platform**: Android SDK / Android Studio
* **Database**: SQLite (`PantryDatabaseHelper`)
* **UI Components**: `RecyclerView`, `LinearLayout`, `ScrollView`, `Spinner`, and `AlertDialog`.

---

## 📂 Core Project Components

* **`PantryListActivity.java`**: The main screen that displays the current pantry inventory using a `RecyclerView` and handles navigation to recipe lists and item modifications.
* **`AddIngredientActivity.java`**: Handles both "add" and "edit" modes for pantry items, complete with input validation and quantity formatting.
* **`PantryDatabaseHelper.java`**: Central SQLite database manager handling table creation (`pantry`, `recipes`, `recipe_ingredients`), CRUD operations, initial recipe seeding, and inventory matching logic.
* **`RecipeListActivity.java`**: Displays matching and suggested recipes based on available pantry items and provides access to the custom recipe creator.
* **`RecipeDetailActivity.java`**: Displays specific recipe instructions, titles, and required ingredient breakdowns.
* **`AddRecipeActivity.java`**: Allows users to input custom recipes and build custom ingredient lists dynamically.
* **`PantryAdapter.java` & `RecipeAdapter.java**`: Custom RecyclerView adapters binding inventory and recipe items to their respective row views.
* **`PantryItem.java`, `Recipe.java`, & `RecipeIngredient.java**`: Data model classes representing individual entities in the app.

---

## 🚀 Getting Started

1. **Clone or Download** the project repository to your local machine.
2. Open **Android Studio**.
3. Select **Open an Existing Project** and choose the project root directory.
4. Allow Gradle to sync dependencies.
5. Connect an Android device or start an emulator (API level 24 or higher recommended).
6. Click the **Run ▶** button to build and launch the app!
