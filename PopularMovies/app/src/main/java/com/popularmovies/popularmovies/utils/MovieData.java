package com.popularmovies.popularmovies.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aditya on 7/8/2017.
 */

public class MovieData implements Parcelable
{
    private String mMovieId;
    private String mOriginalTitle;
    private String mMoviePosterURL;
    private String mPlot;
    private String mUserRating;
    private String mReleaseDate;

    public MovieData(String movieID, String originalTitle, String posterURL, String plot, String userRating, String releaseData)
    {
        mMovieId = movieID;
        mOriginalTitle = originalTitle;
        mMoviePosterURL = posterURL;
        mPlot = plot;
        mUserRating = userRating;
        mReleaseDate = releaseData;
    }

    public String getMovieId()
    {
        return mMovieId;
    }

    public String getOriginalTitle()
    {
        return mOriginalTitle;
    }

    public String getMoviePosterURL()
    {
        return mMoviePosterURL;
    }

    public String getPlot()
    {
        return mPlot;
    }

    public String getUserRating()
    {
        return mUserRating;
    }

    public String getReleaseDate()
    {
        return mReleaseDate;
    }

    @Override public int describeContents()
    {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.mMovieId);
        dest.writeString(this.mOriginalTitle);
        dest.writeString(this.mMoviePosterURL);
        dest.writeString(this.mPlot);
        dest.writeString(this.mUserRating);
        dest.writeString(this.mReleaseDate);
    }

    protected MovieData(Parcel in)
    {
        this.mMovieId = in.readString();
        this.mOriginalTitle = in.readString();
        this.mMoviePosterURL = in.readString();
        this.mPlot = in.readString();
        this.mUserRating = in.readString();
        this.mReleaseDate = in.readString();
    }

    public static final Parcelable.Creator<MovieData> CREATOR = new Parcelable.Creator<MovieData>()
    {
        @Override public MovieData createFromParcel(Parcel source)
        {
            return new MovieData(source);
        }

        @Override public MovieData[] newArray(int size)
        {
            return new MovieData[size];
        }
    };
}
