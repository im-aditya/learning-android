package com.popularmovies.popularmovies.mainscreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.utils.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Aditya on 7/8/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private static final String TAG = MyAdapter.class.getSimpleName();
    private final ListItemClickListener mListItemClickListner;

    private ArrayList<MovieData> mMoviesData;

    public MyAdapter(ListItemClickListener listner)
    {
        mListItemClickListner = listner;
        mMoviesData = new ArrayList<MovieData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_image_view_layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.setImage(mMoviesData.get(position).getMoviePosterURL());
    }

    @Override
    public int getItemCount()
    {
        if(mMoviesData == null)
        {
            return 0;
        }

        return mMoviesData.size();
    }

    public void setMoviesData(MovieData[] moviesData)
    {
        if(moviesData == null)
        {
            return;
        }

        int len = moviesData.length;
        for (int i = 0; i < len; i++)
        {
            mMoviesData.add(moviesData[i]);
        }

        notifyDataSetChanged();
    }

    public void clearMovieListData()
    {
        mMoviesData.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView mImageView;

        public ViewHolder(View view)
        {
            super(view);
            mImageView = (ImageView) view.findViewById(R.id.img_android);
            view.setOnClickListener(this);
        }

        public void setImage(String imageUrl)
        {
            Picasso.with(mImageView.getContext()).load(imageUrl).into(mImageView);
        }

        @Override
        public void onClick(View v)
        {
            int clickedPosition = getAdapterPosition();
            mListItemClickListner.onListItemClickListener(mMoviesData.get(clickedPosition));
        }
    }

    public interface ListItemClickListener
    {
        void onListItemClickListener(MovieData clickedMovieData);
    }
}
