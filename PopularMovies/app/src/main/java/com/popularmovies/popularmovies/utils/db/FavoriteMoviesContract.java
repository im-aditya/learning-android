package com.popularmovies.popularmovies.utils.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Aditya on 8/4/2017.
 */

public final class FavoriteMoviesContract
{
    public static final String AUTHORITY = "com.popularmovies.popularmovies";
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies_list";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private FavoriteMoviesContract()
    {

    }

    public static final class FavoriteMoviesEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favorite_movies_list";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_NAME = "movie_name";
        public static final String COLUMN_MOVIE_USER_RATING = "movie_user_rating";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_MOVIE_PLOT = "movie_plot";
        public static final String COLUMN_MOVIE_POSTER_URL = "movie_poster_url";
    }
}
