package com.popularmovies.popularmovies.mainscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.popularmovies.popularmovies.moviescreen.MovieScreenActivity;
import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.utils.JSONUtils;
import com.popularmovies.popularmovies.utils.MovieData;
import com.popularmovies.popularmovies.utils.NetworkUtils;
import com.popularmovies.popularmovies.utils.db.FavoriteMoviesContract;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MyAdapter.ListItemClickListener
{
    private static String TAG = MainActivity.class.getSimpleName();

    private final String SORT_BY_POPULAR = "popular";
    private final String SORT_BY_TOP_RATED = "top_rated";
    private final String SORT_BY_FAVORITE_MOVIES = "favorite_movies";
    private final String SAVED_MOVIE_LIST = "saved_movie_list";

    private MovieData[] mMoviesData;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private MyAdapter myAdapter;
    private EndlessRecyclerViewScrollListener mScrollListener;

    private String mCurrentSortOrder = SORT_BY_POPULAR;
    private final String FIRST_PAGE_NUMBER = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate..");

        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCurrentSortOrder = sharedPreferences.getString("sort_order", SORT_BY_POPULAR);

        Picasso.with(this).setLoggingEnabled(false);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.rv_grid_view);
        mGridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));

        mRecyclerView.setLayoutManager(mGridLayoutManager);

        myAdapter = new MyAdapter(this);
        mRecyclerView.setAdapter(myAdapter);

        mScrollListener = new EndlessRecyclerViewScrollListener(mGridLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                if(!mCurrentSortOrder.equalsIgnoreCase(SORT_BY_FAVORITE_MOVIES))
                {
                    loadMovieList(mCurrentSortOrder, Integer.toString(page));
                }
            }
        };
        mRecyclerView.addOnScrollListener(mScrollListener);

        if(savedInstanceState != null)
        {
            mMoviesData = (MovieData[]) savedInstanceState.getParcelableArray(SAVED_MOVIE_LIST);
            myAdapter.setMoviesData(mMoviesData);
        }

        loadMovieList(mCurrentSortOrder, FIRST_PAGE_NUMBER);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        Log.d(TAG, "onResume..");

        if(mCurrentSortOrder.equalsIgnoreCase(SORT_BY_FAVORITE_MOVIES))
        {
            resetMovieList();
            loadMovieList(mCurrentSortOrder, FIRST_PAGE_NUMBER);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemIdSelected = item.getItemId();
        Context context = MainActivity.this;
        String message = "";

        switch (itemIdSelected)
        {
            case R.id.action_sort_by_most_populer:
                message = "Sort by most popular";
                resetMovieList();
                loadMovieList(SORT_BY_POPULAR, FIRST_PAGE_NUMBER);
                break;
            case R.id.action_sort_by_top_rated:
                message = "Sort by top rated";
                resetMovieList();
                loadMovieList(SORT_BY_TOP_RATED, FIRST_PAGE_NUMBER);
                break;
            case R.id.action_sort_by_favorite_movies:
                message = "Sort by favorite movies";
                resetMovieList();
                loadMovieList(SORT_BY_FAVORITE_MOVIES, FIRST_PAGE_NUMBER);
                break;
        }

        if(!message.equals(""))
        {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("sort_order", mCurrentSortOrder);
            editor.apply();

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void resetMovieList()
    {
        myAdapter.clearMovieListData();
        mScrollListener.resetState();
    }

    private void loadMovieList(String sortOrder, String pageNum)
    {
        mCurrentSortOrder = sortOrder;
        new FetchMoviesData().execute(sortOrder, pageNum);
    }

    @Override
    public void onListItemClickListener(MovieData clickedMovieData)
    {
        Intent intent = new Intent(MainActivity.this, MovieScreenActivity.class);
        intent.putExtra("movie_data", clickedMovieData);
        startActivity(intent);
    }

    private int calculateNoOfColumns(Context context)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 90;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(SAVED_MOVIE_LIST, mMoviesData);
    }

    class FetchMoviesData extends AsyncTask<String, Void, MovieData[]>
    {

        @Override
        protected MovieData[] doInBackground(String... params)
        {
            MovieData[] moviesData = null;
            String sortByText = params[0];
            String pageNumber = "1";

            if(params.length > 1)
            {
                pageNumber = params[1];
            }

            if(sortByText.equalsIgnoreCase(SORT_BY_FAVORITE_MOVIES))
            {
                Cursor resultCursor = getContentResolver().query(FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        FavoriteMoviesContract.FavoriteMoviesEntry._ID);

                moviesData = new MovieData[resultCursor.getCount()];

                try
                {
                    int i = 0;
                    while(resultCursor.moveToNext())
                    {
                        String movieName = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_NAME));
                        String movieId = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_ID));
                        String posterUrl = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_URL));
                        String plot = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_PLOT));
                        String userRating = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_USER_RATING));
                        String releaseData = resultCursor.getString(resultCursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_RELEASE_DATE));

                        MovieData md = new MovieData(movieId, movieName, posterUrl, plot, userRating, releaseData);
                        moviesData[i] = md;
                        i++;
                    }
                }
                finally
                {
                    resultCursor.close();
                }
            }
            else
            {
                URL movieListUrl = NetworkUtils.buildUrl(sortByText, pageNumber);

                try
                {
                    Log.d(TAG, "Call to API made for page:" + pageNumber);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(movieListUrl);
                    moviesData = JSONUtils.getMovieData(jsonResponse);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            return moviesData;
        }

        @Override
        protected void onPostExecute(MovieData[] moviesData)
        {
            Log.d(TAG, "onPostExecute");
            mMoviesData = moviesData;
            myAdapter.setMoviesData(moviesData);
        }
    }
}
