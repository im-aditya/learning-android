package com.popularmovies.popularmovies.utils.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

/**
 * Created by Aditya on 8/6/2017.
 */

public class FavoriteMoviesContentProvider extends ContentProvider
{
    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIE_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();
    private FavoriteMoviesDbHelper mFavoriteMoviesDbHelper;

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        uriMatcher.addURI(FavoriteMoviesContract.AUTHORITY, FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#", FAVORITE_MOVIE_WITH_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate()
    {
        Context context = getContext();
        mFavoriteMoviesDbHelper = new FavoriteMoviesDbHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        final SQLiteDatabase sqLiteDatabase = mFavoriteMoviesDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match)
        {
            case FAVORITE_MOVIES:
            case FAVORITE_MOVIE_WITH_ID:
                returnCursor = sqLiteDatabase.query(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return returnCursor;
    }

    @Override
    public String getType(Uri uri)
    {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        final SQLiteDatabase sqLiteDatabase = mFavoriteMoviesDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match)
        {
            case FAVORITE_MOVIES:
                long id = sqLiteDatabase.insert(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, null, values);
                if(id>0)
                {
                    returnUri = ContentUris.withAppendedId(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, id);
                }
                else
                {
                    throw new SQLiteException("Failed to insert row into:" + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknwon URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        final SQLiteDatabase db = mFavoriteMoviesDbHelper.getWritableDatabase();
        int rowsDeleted;


        if (selection == null)
            selection = "1";

        switch (sUriMatcher.match(uri))
        {
            case FAVORITE_MOVIES:
                rowsDeleted = db.delete(FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        return 0;
    }
}
