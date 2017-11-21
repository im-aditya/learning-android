package com.bt.bakingtime.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bt.bakingtime.R;
import com.bt.bakingtime.activities.recipedetaillist.RecipeDetailListActivity;
import com.bt.bakingtime.data.Recipe;
import com.bt.bakingtime.utils.JSONUtils;
import com.bt.bakingtime.utils.NetworkUtils;

import java.io.IOException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{
    private static final String KEY_RECIPE_LIST_STATE = "keyRecipeListState";

    private ArrayList<Recipe> mRecipiesData;
    private RecyclerView mRecipeListRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private RecipeListAdapter mRecipeListAdapter;
    private Parcelable mRecipeListState;
    private TextView mErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                     .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                                     .setFontAttrId(R.attr.fontPath)
                                     .build());

        setContentView(R.layout.activity_main);

        mErrorMessage = (TextView) this.findViewById(R.id.tv_error_message);

        getRecipiesData();
        mRecipeListAdapter = new RecipeListAdapter(this, mRecipiesData);
        mRecipeListAdapter.setOnRecipeItemClickListener(new RecipeListAdapter.OnRecipeItemClickListener()
        {
            @Override
            public void onRecipeItemClicked(Recipe recipeClicked)
            {
                Intent intent = new Intent(MainActivity.this, RecipeDetailListActivity.class);
                intent.putExtra("recipe_data", recipeClicked);
                startActivity(intent);
            }
        });

        mRecipeListRecyclerView = (RecyclerView) this.findViewById(R.id.rv_recipe_list);
        mRecipeListRecyclerView.setAdapter(mRecipeListAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecipeListRecyclerView.setLayoutManager(mLinearLayoutManager);
    }

    @Override protected void onPause()
    {
        super.onPause();

        if(mRecipeListState != null)
        {
            mLinearLayoutManager.onRestoreInstanceState(mRecipeListState);
        }
    }

    @Override protected void onResume()
    {
        super.onResume();

        if(mRecipeListState != null)
        {
            mLinearLayoutManager.onRestoreInstanceState(mRecipeListState);
        }
    }

    private void getRecipiesData()
    {
        mRecipiesData = new ArrayList<>();

        new AsyncTask<Void, Void, ArrayList<Recipe>>()
        {
            @Override
            protected ArrayList<Recipe> doInBackground(Void... params)
            {
                try
                {
                    String jsonStringResponse = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildUrl());
                    mRecipiesData = JSONUtils.getRecipiesData(jsonStringResponse);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                return mRecipiesData;
            }

            @Override
            protected void onPostExecute(ArrayList<Recipe> recipes)
            {
                super.onPostExecute(recipes);

                if(recipes.size() == 0)
                {
                    mErrorMessage.setText(R.string.error_message);
                    mErrorMessage.setVisibility(View.VISIBLE);
                }
                else
                {
                    mErrorMessage.setVisibility(View.GONE);
                }

                mRecipeListAdapter.setRecipesData(recipes);

                if(mRecipeListState != null)
                {
                    mLinearLayoutManager.onRestoreInstanceState(mRecipeListState);
                }
            }
        }.execute();
    }

    @Override protected void attachBaseContext(Context newBase)
    {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        mRecipeListState = mLinearLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_RECIPE_LIST_STATE, mRecipeListState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null)
        {
            mRecipeListState = savedInstanceState.getParcelable(KEY_RECIPE_LIST_STATE);
        }
    }
}
