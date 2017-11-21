package com.bt.bakingtime.utils;

import com.bt.bakingtime.data.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aditya on 7/8/2017.
 */

public class JSONUtils
{
    public static ArrayList<Recipe> getRecipiesData(String recipiesJsonStr)
    {
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        JSONArray recipeResultsArray = getResultsArray(recipiesJsonStr);

        try
        {
            for (int i = 0; i < recipeResultsArray.length(); i++)
            {
                JSONObject recipeObject = recipeResultsArray.getJSONObject(i);
                String id = recipeObject.getString("id");
                String recipeName = recipeObject.getString("name");
                String servings = recipeObject.getString("servings");
                String imageUrl = recipeObject.getString("image");

                Recipe recipe = new Recipe(id, recipeName, servings, imageUrl);

                JSONArray ingredientsArray = recipeObject.getJSONArray("ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++)
                {
                    JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                    String quantity = ingredientObject.getString("quantity");
                    String measure = ingredientObject.getString("measure");
                    String ingredient = ingredientObject.getString("ingredient");
                    recipe.addIngredient(new Recipe.Ingredients(quantity, measure, ingredient));
                }

                JSONArray recipeStepsArray = recipeObject.getJSONArray("steps");
                for (int k = 0; k < recipeStepsArray.length(); k++)
                {
                    JSONObject recipeStepObject = recipeStepsArray.getJSONObject(k);
                    String stepId = recipeStepObject.getString("id");
                    String shortDes = recipeStepObject.getString("shortDescription");
                    String longDes = recipeStepObject.getString("description");
                    String videoUrl = recipeStepObject.getString("videoURL");
                    String thumbnailUrl = recipeStepObject.getString("thumbnailURL");
                    recipe.addRecipeStep(new Recipe.RecipeStep(stepId, recipeName, shortDes, longDes, videoUrl, thumbnailUrl));
                }

                recipeArrayList.add(recipe);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return recipeArrayList;
    }

    private static JSONArray getResultsArray(String jsonString)
    {
        JSONArray resultsArray = null;

        try
        {
            resultsArray = new JSONArray(jsonString);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return resultsArray;
    }
}
