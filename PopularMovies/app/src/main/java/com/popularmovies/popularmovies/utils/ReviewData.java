package com.popularmovies.popularmovies.utils;

/**
 * Created by Aditya on 7/31/2017.
 */

public class ReviewData
{
    private String mReviewId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public ReviewData(String reviewId, String author, String content, String url)
    {
        mReviewId = reviewId;
        mAuthor = author;
        mContent = content;
        mUrl = url;
    }


    public String getReviewId()
    {
        return mReviewId;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public String getContent()
    {
        return mContent;
    }

    public String getUrl()
    {
        return mUrl;
    }
}
