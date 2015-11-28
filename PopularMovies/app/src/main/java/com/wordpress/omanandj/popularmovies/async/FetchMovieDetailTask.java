package com.wordpress.omanandj.popularmovies.async;

import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

/**
 * Created by ojha on 29/11/15.
 */
public class FetchMovieDetailTask extends AsyncTask<String, Void, MovieDetail>
{

    private static final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

    private IAsyncTaskResponse<MovieDetail> asyncTaskResponse;

    public FetchMovieDetailTask(IAsyncTaskResponse<MovieDetail> asyncTaskResponse)
    {
        this.asyncTaskResponse = asyncTaskResponse;
    }

    @Override
    protected MovieDetail doInBackground(String... params)
    {

        Log.v(LOG_TAG, "Fetching popular popular movies..");

        assert params.length == 1;

        String movieId = params[0];

        Log.v(LOG_TAG, "MovieDetail will be fetched for id " + movieId);

        IMovieDbService movieDbService = MovieDbService.getInstance();
        return movieDbService.getMovieDetail(movieId);
    }

    @Override
    protected void onPostExecute(MovieDetail movieDetail)
    {
        asyncTaskResponse.processFinish(movieDetail);
        super.onPostExecute(movieDetail);

    }
}
