package com.popularmovies.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by Aditya on 7/8/2017.
 */

public class JSONUtils
{
    public static MovieData[] getMovieData(String movieDetailsStr)
    {
        MovieData[] moviesData = null;
        JSONArray movieResultsArray = null;

        try
        {
            movieResultsArray = getResultsArray(movieDetailsStr);
            moviesData = new MovieData[movieResultsArray.length()];

            for (int i=0; i < movieResultsArray.length(); i++)
            {
                JSONObject movieObject = movieResultsArray.getJSONObject(i);
                String id = movieObject.getString("id");
                String originalTitle = movieObject.getString("original_title");
                String posterURL = "http://image.tmdb.org/t/p/w185" + movieObject.getString("poster_path");
                String plot = movieObject.getString("overview");
                String userRating = movieObject.getString("vote_average");
                String releaseData = movieObject.getString("release_date");
                MovieData md = new MovieData(id, originalTitle, posterURL, plot, userRating, releaseData);
                moviesData[i] = md;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return moviesData;
    }

    public static ReviewData[] getMovieReviewsData(String jsonResponse)
    {
        ReviewData[] reviewsData = null;
        JSONArray resultsArray = null;

        try
        {
            resultsArray = getResultsArray(jsonResponse);
            reviewsData = new ReviewData[resultsArray.length()];

            for (int i=0; i < resultsArray.length(); i++)
            {
                JSONObject reviewObject = resultsArray.getJSONObject(i);
                String id = reviewObject.getString("id");
                String author = reviewObject.getString("author");
                String content = reviewObject.getString("content");
                String url = reviewObject.getString("url");
                ReviewData reviewData = new ReviewData(id, author, content, url);
                reviewsData[i] = reviewData;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return reviewsData;
    }

    public static TrailerData[] getMovieTrailersData(String jsonResponse)
    {
        TrailerData[] trailersData = null;
        JSONArray resultsArray = null;

        try
        {
            resultsArray = getResultsArray(jsonResponse);
            trailersData = new TrailerData[resultsArray.length()];

            for (int i=0; i < resultsArray.length(); i++)
            {
                JSONObject reviewObject = resultsArray.getJSONObject(i);
                String id = reviewObject.getString("id");
                String name = reviewObject.getString("name");
                String site = reviewObject.getString("site");
                String key = reviewObject.getString("key");
                TrailerData td = new TrailerData(id, name, site, key);
                trailersData[i] = td;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return trailersData;
    }

    private static JSONArray getResultsArray(String jsonString)
    {
        final String OWM_MESSAGE_CODE = "cod";
        JSONObject inputJson = null;
        JSONArray resultsArray = null;

        try
        {
            inputJson = new JSONObject(jsonString);

            /* Is there an error? */
            if (inputJson.has(OWM_MESSAGE_CODE))
            {
                int errorCode = inputJson.getInt(OWM_MESSAGE_CODE);

                switch (errorCode)
                {
                    case HttpURLConnection.HTTP_OK:
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        return null;
                    default:
                        /* Server probably down */
                        return null;
                }
            }

            resultsArray = inputJson.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return resultsArray;
    }
}
