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
import com.wordpress.omanandj.popularmovies.model.MovieReview;

/**
 * Created by ojha on 10/01/16.
 */
public class MovieReviewAdapter extends ArrayAdapter<MovieReview>
{

    private static final String LOG_TAG = MovieReview.class.getSimpleName();

    public MovieReviewAdapter(Context context, List<MovieReview> objects)
    {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        final MovieReview movieReview = getItem(position);

        Log.v(LOG_TAG, "Fetching view for index " + position);
        Log.v(LOG_TAG, "Fetching view for movie " + movieReview.toString());

        View view = convertView;
        ViewHolder viewHolder;

        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.review_list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) view.getTag();
        viewHolder.reviewContent.setText(movieReview.getContent());
        viewHolder.reviewAuthor.setText(movieReview.getAuthor());
        return view;
    }

    public static class ViewHolder
    {
        public final ImageView reviewIcon;
        public final TextView reviewContent;
        public final TextView reviewAuthor;

        public ViewHolder(View view)
        {
            reviewIcon = (ImageView) view.findViewById(R.id.review_list_item_icon);
            reviewContent = (TextView) view.findViewById(R.id.list_item_movie_review_content);
            reviewAuthor = (TextView) view.findViewById(R.id.list_item_movie_review_author);
        }
    }
}
