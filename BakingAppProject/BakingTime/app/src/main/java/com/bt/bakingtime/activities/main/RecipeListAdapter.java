package com.bt.bakingtime.activities.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bt.bakingtime.R;
import com.bt.bakingtime.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Aditya on 8/17/2017.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder>
{
    private ArrayList<Recipe> mRecipies;
    private Context mContext;
    private OnRecipeItemClickListener onRecipeItemClickListener;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes)
    {
        mContext = context;
        mRecipies = recipes;
    }

    public void setOnRecipeItemClickListener(OnRecipeItemClickListener recipeItemClickListener)
    {
        onRecipeItemClickListener = recipeItemClickListener;
    }

    @Override
    public RecipeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View recipeCardView = layoutInflater.inflate(R.layout.item_layout_recipe_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeCardView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.ViewHolder holder, int position)
    {
        Recipe recipe = mRecipies.get(position);
        holder.setRecipe(recipe);
    }

    @Override
    public int getItemCount()
    {
        return mRecipies.size();
    }

    public void setRecipesData(ArrayList<Recipe> recipies)
    {
        mRecipies.clear();
        mRecipies.addAll(recipies);

        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private Recipe mRecipe;

        public ViewHolder(View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(onRecipeItemClickListener != null)
                    {
                        onRecipeItemClickListener.onRecipeItemClicked(mRecipe);
                    }
                }
            });
        }

        public Recipe getRecipe()
        {
            return mRecipe;
        }

        public void setRecipe(Recipe recipe)
        {
            this.mRecipe = recipe;

            ImageView recipeImageView = (ImageView) itemView.findViewById(R.id.recipe_image);
            TextView recipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name);
            TextView recipeServingsTextView = (TextView) itemView.findViewById(R.id.recipe_servings);

            recipeNameTextView.setText(mRecipe.getRecipeName());
            recipeServingsTextView.setText("Servings : " + mRecipe.getServings());

            if(!mRecipe.getImageUrl().isEmpty())
            {
                Picasso.with(mContext)
                        .load(mRecipe.getImageUrl())
                        .into(recipeImageView);
            }
        }
    }

    public interface OnRecipeItemClickListener
    {
        void onRecipeItemClicked(Recipe recipeClicked);
    }
}
