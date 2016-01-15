package com.wordpress.omanandj.popularmovies.adapter;

import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

/**
 * Created by ojha on 28/11/15.
 */
public class MoviePosterAdapter extends ArrayAdapter<MoviePoster>
{
    private static final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();
    private IMovieDbService movieDbService;

    public MoviePosterAdapter(Activity context, IMovieDbService movieDbService, List<MoviePoster> moviePosters)
    {
        super(context, 0, moviePosters);
        this.movieDbService = movieDbService;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView = (ImageView) convertView;

        if (imageView == null) {
            imageView = new ImageView(parent.getContext());
            imageView.setPadding(0, 0, 0, 0);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        final MoviePoster moviePoster = getItem(position);
        Log.v(LOG_TAG, "Fetching view for index " + position);
        Log.v(LOG_TAG, "Fetching view for movie " + moviePoster.toString());

        Picasso.with(parent.getContext()).load(movieDbService.getMoviePosterUrl(moviePoster.getPosterPath()))
                .into(imageView);

        return imageView;
    }
}
