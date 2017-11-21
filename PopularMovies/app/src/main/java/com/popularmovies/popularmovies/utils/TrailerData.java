package com.popularmovies.popularmovies.utils;

/**
 * Created by Aditya on 8/1/2017.
 */

public class TrailerData
{
    private String mTrailerId;
    private String mName;
    private String mSite;
    private String mKey;

    public TrailerData(String trailerId, String name, String site, String key)
    {

        this.mTrailerId = trailerId;
        this.mName = name;
        this.mSite = site;
        this.mKey = key;
    }

    public String getName()
    {
        return mName;
    }

    public String getKey()
    {
        return mKey;
    }
}
