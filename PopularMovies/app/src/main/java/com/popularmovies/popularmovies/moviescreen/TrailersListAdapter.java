package com.popularmovies.popularmovies.moviescreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.utils.TrailerData;

/**
 * Created by Aditya on 7/28/2017.
 */

public class TrailersListAdapter extends ArrayAdapter<TrailerData>
{
    private final String TAG = TrailersListAdapter.class.getSimpleName();

    private final Context context;
    private TrailerData[] trailersData;

    public TrailersListAdapter(Context context)
    {
        super(context, -1);

        this.context = context;
        this.trailersData = null;
    }

    @Override
    public int getCount()
    {
        if(trailersData != null)
        {
            return trailersData.length;
        }

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(R.layout.trailers_list_item_layout, parent, false);

        TextView trailerTextView = (TextView) rowView.findViewById(R.id.trailer_text_view);

        if(trailersData != null)
        {
            trailerTextView.setText(trailersData[position].getName());
        }

        return rowView;
    }

    public void setTrailersData(TrailerData[] trailersData)
    {
        this.trailersData = trailersData;
        notifyDataSetChanged();
    }

    public TrailerData getTrailerData(int position)
    {
        return trailersData[position];
    }
}
