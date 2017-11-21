package com.bt.bakingtime.activities.recipedetaillist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bt.bakingtime.R;
import com.bt.bakingtime.activities.recipestepdetails.RecipeStepDetailFragment;
import com.bt.bakingtime.activities.recipestepdetails.StepDetailsActivity;
import com.bt.bakingtime.data.Recipe;

/**
 * Created by Aditya on 8/22/2017.
 */

public class RecipeDetailListActivity extends AppCompatActivity implements RecipeDetailListFragment.OnStepClickListener
{
    private Recipe mRecipe;
    private Toast mToast;

    private RecipeStepDetailFragment mRecipeStepDetailFragment;
    private boolean mIsTablet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail_list);

        mIsTablet = false;

        mRecipe = getIntent().getParcelableExtra("recipe_data");
        Toast.makeText(this, "Recipe: " + mRecipe.getRecipeName(), Toast.LENGTH_SHORT).show();

        TextView mRecipeNameTextView = (TextView) this.findViewById(R.id.tv_recipe_name);
        mRecipeNameTextView.setText(mRecipe.getRecipeName());

        RecipeDetailListFragment recipeDetailListFragment = (RecipeDetailListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipe_detail_list);
        recipeDetailListFragment.setRecipe(mRecipe);

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_recipe_step_detail) != null)
        {
            mIsTablet = true;

            mRecipeStepDetailFragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipe_step_detail);
            mRecipeStepDetailFragment.setRecipeStepDetail(mRecipe.getRecipeSteps().get(0));
        }

        ImageButton backButton = (ImageButton) this.findViewById(R.id.ib_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RecipeDetailListActivity.this.finish();
            }
        });
    }

    @Override
    public void onStepSelected(Recipe.RecipeStep recipeStep)
    {
        mToast = Toast.makeText(this, "Step: " + recipeStep.getShortDescription(), Toast.LENGTH_SHORT);
        mToast.show();

        if(mIsTablet)
        {
            mRecipeStepDetailFragment.setRecipeStepDetail(recipeStep);
        }
        else
        {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("recipe_step", recipeStep);
            startActivity(intent);
        }
    }
}
