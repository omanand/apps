package com.wordpress.omanandj.popularmovies.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wordpress.omanandj.popularmovies.R;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;

/**
 * Created by ojha on 08/01/16.
 */
public class MovieTrailerAdapter extends ArrayAdapter<MovieTrailer>
{
    private static final String LOG_TAG = MovieTrailer.class.getSimpleName();

    public MovieTrailerAdapter(Context context, List<MovieTrailer> movieTrailers)
    {
        super(context, 0, movieTrailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MovieTrailer movieTrailer = getItem(position);

        Log.v(LOG_TAG, "Fetching view for index " + position);
        Log.v(LOG_TAG, "Fetching view for movie " + movieTrailer.toString());

        View view = convertView;
        ViewHolder viewHolder;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.trailer_list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.trailerName.setText(movieTrailer.getName());

        return view;
    }

    public static class ViewHolder
    {
        public final ImageView playIcon;
        public final TextView trailerName;

        public ViewHolder(View view)
        {
            playIcon = (ImageView) view.findViewById(R.id.list_item_icon);
            trailerName = (TextView) view.findViewById(R.id.list_item_trailer_name_view);
        }
    }

}
