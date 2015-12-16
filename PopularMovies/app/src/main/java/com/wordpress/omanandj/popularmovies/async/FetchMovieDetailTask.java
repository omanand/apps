package com.wordpress.omanandj.popularmovies.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.network.NetworkUtils;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

/**
 * Created by ojha on 29/11/15.
 */
public class FetchMovieDetailTask extends AsyncTask<String, Void, AsyncTaskResult<MovieDetail>>
{

    private static final String LOG_TAG = FetchMovieDetailTask.class.getSimpleName();

    private final IAsyncTaskResponseHandler<AsyncTaskResult<MovieDetail>> asyncTaskResponse;
    private final Context mContext;

    IMovieDbService movieDbService;


    public FetchMovieDetailTask(IAsyncTaskResponseHandler<AsyncTaskResult<MovieDetail>> asyncTaskResponse,
            IMovieDbService movieDbService, Context context)
    {
        this.asyncTaskResponse = asyncTaskResponse;
        this.mContext = context;
        this.movieDbService = movieDbService;
    }

    @Override
    protected AsyncTaskResult<MovieDetail> doInBackground(String... params)
    {

        Log.v(LOG_TAG, "Fetching popular popular movies..");

        assert params.length == 1;

        if (!NetworkUtils.isOnline(mContext)) {
            return new AsyncTaskResult<>("Network Connectivity Not Available. Please try after sometime");
        }

        String movieId = params[0];

        Log.v(LOG_TAG, "MovieDetail will be fetched for id " + movieId);

        MovieDetail movieDetail = movieDbService.getMovieDetail(movieId);
        return new AsyncTaskResult<>(movieDetail);
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<MovieDetail> result)
    {
        asyncTaskResponse.processFinish(result);

    }
}
