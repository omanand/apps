package com.wordpress.omanandj.popularmovies.async;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.network.NetworkUtils;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

/**
 * Created by ojha on 28/11/15.
 */
public class FetchMoviePostersTask extends AsyncTask<MoviesSortOrder, Void, AsyncTaskResult<List<MoviePoster>>>
{
    private static final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

    private final IAsyncTaskResponseHandler<AsyncTaskResult<List<MoviePoster>>> mAsyncTaskResponse;
    private final Context mContext;

    public FetchMoviePostersTask(IAsyncTaskResponseHandler<AsyncTaskResult<List<MoviePoster>>> asyncTaskResponse,
            Context context)
    {
        this.mAsyncTaskResponse = asyncTaskResponse;
        this.mContext = context;
    }

    @Override
    protected AsyncTaskResult<List<MoviePoster>> doInBackground(MoviesSortOrder... params)
    {
        Log.v(LOG_TAG, "Fetching popular popular movies..");

        if (!NetworkUtils.isOnline(mContext)) {
            return new AsyncTaskResult("Network Connectivity Not Available. Please try after sometime");
        }
        MoviesSortOrder moviesSortOrder = params[0];

        Log.v(LOG_TAG, "MovieDetail will be fetched sorted orde by " + moviesSortOrder.getSortOrder());

        IMovieDbService movieDbService = MovieDbService.getInstance();
        List<MoviePoster> moviePosters = movieDbService.getMoviePosters(moviesSortOrder);
        return new AsyncTaskResult(moviePosters);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<List<MoviePoster>> result)
    {
        mAsyncTaskResponse.processFinish(result);
    }
}
