package com.popularmovies.popularmovies.moviescreen;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.utils.JSONUtils;
import com.popularmovies.popularmovies.utils.MovieData;
import com.popularmovies.popularmovies.utils.NetworkUtils;
import com.popularmovies.popularmovies.utils.ReviewData;
import com.popularmovies.popularmovies.utils.TrailerData;
import com.popularmovies.popularmovies.utils.db.FavoriteMoviesContract;
import com.popularmovies.popularmovies.utils.db.FavoriteMoviesDbHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class MovieScreenActivity extends AppCompatActivity
{
    private static final String TAG = MovieScreenActivity.class.getSimpleName();

    private MovieData mMovieData;
    private ReviewsListAdapter mReviewsListAdapter;
    private TrailersListAdapter mTrailersListAdapter;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null && intentThatStartedThisActivity.hasExtra("movie_data"))
        {
            mMovieData = intentThatStartedThisActivity.getParcelableExtra("movie_data");

            TextView movie_name_tv = (TextView) this.findViewById(R.id.movie_name);
            movie_name_tv.setText(mMovieData.getOriginalTitle());

            TextView user_rating_tv = (TextView) this.findViewById(R.id.user_rating);
            user_rating_tv.setText(getString(R.string.user_rating, mMovieData.getUserRating()));

            TextView release_date_tv = (TextView) this.findViewById(R.id.release_date);
            release_date_tv.setText(getString(R.string.release_date, mMovieData.getReleaseDate()));

            TextView plot_tv = (TextView) this.findViewById(R.id.movie_plot);
            plot_tv.setText(mMovieData.getPlot());

            ImageView poster_iv = (ImageView) this.findViewById(R.id.movie_poster);
            Picasso.with(poster_iv.getContext()).load(mMovieData.getMoviePosterURL()).resize(100, 150).into(poster_iv);

            addTrailerList();
            addReviewsList();

            new FetchMoviesReviewsData().execute(mMovieData.getMovieId());
            new FetchMoviesTrailersData().execute(mMovieData.getMovieId());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.favorite_menu, menu);

        final MenuItem action_favorite = menu.findItem(R.id.action_add_to_favorite);

        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Void... params)
            {
                Cursor cursor = getBaseContext().getContentResolver().query (
                        FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                        null,   // projection
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = ?", // selection
                        new String[] { mMovieData.getMovieId() },   // selectionArgs
                        null    // sort order
                                                                            );

                int numRows = cursor.getCount();
                cursor.close();

                isFavorite = (numRows == 1);
                return isFavorite;
            }

            @Override
            protected void onPostExecute(Boolean isFavorited)
            {
                action_favorite.setIcon(isFavorited ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
            }
        }.execute();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemIdSelected = item.getItemId();
        if(itemIdSelected == R.id.action_add_to_favorite)
        {
            if(isFavorite)
            {
                //remove from favorite list
                getContentResolver().delete(
                        FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                        FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{mMovieData.getMovieId()});

                isFavorite = false;
                item.setIcon(R.drawable.ic_star_border_black_24dp);
                Toast.makeText(this, "Movie removed from favorities.", Toast.LENGTH_SHORT).show();
                return true;
            }
            else
            {
                //add to favorite list
                ContentValues contentValues = new ContentValues();
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID, mMovieData.getMovieId());
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_NAME, mMovieData.getOriginalTitle());
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_PLOT, mMovieData.getPlot());
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_URL, mMovieData.getMoviePosterURL());
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_RELEASE_DATE, mMovieData.getReleaseDate());
                contentValues.put(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING, mMovieData.getUserRating());

                Uri uri = null;

                try
                {
                    uri = getContentResolver().insert(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI, contentValues);
                }
                catch (SQLiteException e)
                {
                    Toast.makeText(getBaseContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return false;
                }

                if(uri != null)
                {
                    isFavorite = true;
                    item.setIcon(R.drawable.ic_star_black_24dp);
                    Toast.makeText(getBaseContext(), "Movie added to favorities.", Toast.LENGTH_LONG).show();
                }

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void addTrailerList()
    {
        ListView trailerListView = (ListView) this.findViewById(R.id.listview_trailer);
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(mTrailersListAdapter != null)
                {
                    TrailerData td = mTrailersListAdapter.getTrailerData(position);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + td.getKey())));
                }
            }
        });
        mTrailersListAdapter = new TrailersListAdapter(this);
        trailerListView.setAdapter(mTrailersListAdapter);
    }

    private void addReviewsList()
    {
        ListView reviewsListView = (ListView) this.findViewById(R.id.listview_reviews);

        mReviewsListAdapter = new ReviewsListAdapter(this);
        reviewsListView.setAdapter(mReviewsListAdapter);
    }

    class FetchMoviesReviewsData extends AsyncTask<String, Void, ReviewData[]>
    {

        @Override
        protected ReviewData[] doInBackground(String... params)
        {
            ReviewData[] reviewsData = null;
            String movieId = "";

            if(params.length == 1)
            {
                movieId = params[0];
            }

            URL movieListUrl = NetworkUtils.buildUrlForReviews(movieId);

            try
            {
                Log.d(TAG, "Call to API made for movie_id: " + movieId);
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                reviewsData = JSONUtils.getMovieReviewsData(jsonResponse);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return reviewsData;
        }

        @Override
        protected void onPostExecute(ReviewData[] reviewsData)
        {
            Log.d(TAG, "onPostExecute");
            mReviewsListAdapter.setReviewsData(reviewsData);
        }
    }

    class FetchMoviesTrailersData extends AsyncTask<String, Void, TrailerData[]>
    {

        @Override
        protected TrailerData[] doInBackground(String... params)
        {
            TrailerData[] trailersData = null;
            String movieId = "";

            if(params.length == 1)
            {
                movieId = params[0];
            }

            URL movieListUrl = NetworkUtils.buildUrlForTrailers(movieId);

            try
            {
                Log.d(TAG, "Call to API made for movie_id: " + movieId);
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                trailersData = JSONUtils.getMovieTrailersData(jsonResponse);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return trailersData;
        }

        @Override
        protected void onPostExecute(TrailerData[] trailersData)
        {
            Log.d(TAG, "onPostExecute");
            mTrailersListAdapter.setTrailersData(trailersData);
        }
    }
}
