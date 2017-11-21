package com.bt.bakingtime.activities.recipestepdetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bt.bakingtime.R;
import com.bt.bakingtime.data.Recipe;

public class StepDetailsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent intent = getIntent();
        Recipe.RecipeStep recipeStep = intent.getParcelableExtra("recipe_step");

        TextView mRecipeNameTextView = (TextView) this.findViewById(R.id.tv_recipe_name);
        mRecipeNameTextView.setText(recipeStep.getRecipeName());

        ImageButton backButton = (ImageButton) this.findViewById(R.id.ib_back_button);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StepDetailsActivity.this.finish();
            }
        });

        RecipeStepDetailFragment recipeStepDetailFragment = (RecipeStepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_recipe_step_detail);
        recipeStepDetailFragment.setRecipeStepDetail(recipeStep);
    }
}
