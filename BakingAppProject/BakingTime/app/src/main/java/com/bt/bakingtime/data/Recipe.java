package com.bt.bakingtime.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Aditya on 8/17/2017.
 */

public class Recipe implements Parcelable
{
    private String mId;
    private String mRecipeName;
    private String mServings;
    private String mImageUrl;

    private ArrayList<Ingredients> mIngrediants;
    private ArrayList<RecipeStep> mRecipeSteps;

    public Recipe(String id, String recipeName, String servings, String imageUrl)
    {
        mId = id;
        mRecipeName = recipeName;
        mServings = servings;
        mImageUrl = imageUrl;

        mIngrediants = new ArrayList<Ingredients>();
        mRecipeSteps = new ArrayList<RecipeStep>();
    }

    public void addIngredient(Ingredients ingredients)
    {
        mIngrediants.add(ingredients);
    }

    public void addRecipeStep(RecipeStep recipeStep)
    {
        mRecipeSteps.add(recipeStep);
    }

    public String getId()
    {
        return mId;
    }

    public String getRecipeName()
    {
        return mRecipeName;
    }

    public String getServings()
    {
        return mServings;
    }

    public String getImageUrl()
    {
        return mImageUrl;
    }

    public ArrayList<Ingredients> getIngrediants()
    {
        return mIngrediants;
    }

    public ArrayList<RecipeStep> getRecipeSteps()
    {
        return mRecipeSteps;
    }

    public static class Ingredients implements Parcelable
    {
        private String mQuantity;
        private String mMeasure;
        private String mIngredient;

        public Ingredients(String quantity, String measure, String ingredient)
        {
            mQuantity = quantity;
            mMeasure = measure;
            mIngredient = ingredient;
        }

        public String getQuantity()
        {
            return mQuantity;
        }

        public String getMeasure()
        {
            return mMeasure;
        }

        public String getIngredient()
        {
            return mIngredient;
        }

        @Override public int describeContents()
        {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(this.mQuantity);
            dest.writeString(this.mMeasure);
            dest.writeString(this.mIngredient);
        }

        protected Ingredients(Parcel in)
        {
            this.mQuantity = in.readString();
            this.mMeasure = in.readString();
            this.mIngredient = in.readString();
        }

        public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>()
        {
            @Override public Ingredients createFromParcel(Parcel source)
            {
                return new Ingredients(source);
            }

            @Override public Ingredients[] newArray(int size)
            {
                return new Ingredients[size];
            }
        };
    }

    public static class RecipeStep implements Parcelable
    {
        private String mId;
        private String mRecipeName;
        private String mShortDescription;
        private String mLongDescription;
        private String mVideoUrl;
        private String mThumbnailUrl;

        public RecipeStep(String id, String recipeName, String shortDescription, String longDescription, String videoUrl, String thumbnailUrl)
        {
            mId = id;
            mRecipeName = recipeName;
            mShortDescription = shortDescription;
            mLongDescription = longDescription;
            mVideoUrl = videoUrl;
            mThumbnailUrl = thumbnailUrl;
        }

        public String getId()
        {
            return mId;
        }

        public String getShortDescription()
        {
            return mShortDescription;
        }

        public String getLongDescription()
        {
            return mLongDescription;
        }

        public String getVideoUrl()
        {
            return mVideoUrl;
        }

        public String getThumbnailUrl()
        {
            return mThumbnailUrl;
        }

        public String getRecipeName()
        {
            return mRecipeName;
        }

        @Override public int describeContents()
        {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags)
        {
            dest.writeString(this.mId);
            dest.writeString(this.mRecipeName);
            dest.writeString(this.mShortDescription);
            dest.writeString(this.mLongDescription);
            dest.writeString(this.mVideoUrl);
            dest.writeString(this.mThumbnailUrl);
        }

        protected RecipeStep(Parcel in)
        {
            this.mId = in.readString();
            this.mRecipeName = in.readString();
            this.mShortDescription = in.readString();
            this.mLongDescription = in.readString();
            this.mVideoUrl = in.readString();
            this.mThumbnailUrl = in.readString();
        }

        public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>()
        {
            @Override public RecipeStep createFromParcel(Parcel source)
            {
                return new RecipeStep(source);
            }

            @Override public RecipeStep[] newArray(int size)
            {
                return new RecipeStep[size];
            }
        };
    }

    @Override public int describeContents()
    {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.mId);
        dest.writeString(this.mRecipeName);
        dest.writeString(this.mServings);
        dest.writeString(this.mImageUrl);
        dest.writeTypedList(this.mIngrediants);
        dest.writeTypedList(this.mRecipeSteps);
    }

    protected Recipe(Parcel in)
    {
        this.mId = in.readString();
        this.mRecipeName = in.readString();
        this.mServings = in.readString();
        this.mImageUrl = in.readString();
        this.mIngrediants = new ArrayList<Ingredients>();
        in.readTypedList(this.mIngrediants, Ingredients.CREATOR);
        this.mRecipeSteps = new ArrayList<RecipeStep>();
        in.readTypedList(this.mRecipeSteps, RecipeStep.CREATOR);
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>()
    {
        @Override public Recipe createFromParcel(Parcel source)
        {
            return new Recipe(source);
        }

        @Override public Recipe[] newArray(int size)
        {
            return new Recipe[size];
        }
    };
}
