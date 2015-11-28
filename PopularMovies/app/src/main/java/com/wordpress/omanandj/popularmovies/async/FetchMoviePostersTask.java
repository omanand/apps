package com.wordpress.omanandj.popularmovies.async;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.adapter.MoviePosterAdapter;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

/**
 * Created by ojha on 28/11/15.
 */
public class FetchMoviePostersTask extends AsyncTask<MoviesSortOrder, Void, List<MoviePoster>>
{
    private static final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

    private final MoviePosterAdapter mMoviePosterAdapter;

    public FetchMoviePostersTask(MoviePosterAdapter mMoviePosterAdapter)
    {
        this.mMoviePosterAdapter = mMoviePosterAdapter;
    }

    @Override
    protected List<MoviePoster> doInBackground(MoviesSortOrder... params)
    {
        Log.v(LOG_TAG, "Fetching popular popular movies..");

        MoviesSortOrder moviesSortOrder = params[0];

        Log.v(LOG_TAG, "MovieDetail will be fetched sorted orde by " + moviesSortOrder.getSortOrder());

        IMovieDbService movieDbService = MovieDbService.getInstance();
        return movieDbService.getMoviePosters(moviesSortOrder);
    }

    @Override
    protected void onPostExecute(List<MoviePoster> moviePosters)
    {
        if(null != mMoviePosterAdapter && null != moviePosters) {
            mMoviePosterAdapter.clear();
            for (int i = 0; i < moviePosters.size(); i++) {
                mMoviePosterAdapter.add(moviePosters.get(i));
            }
            mMoviePosterAdapter.notifyDataSetChanged();

        }
    }
}
