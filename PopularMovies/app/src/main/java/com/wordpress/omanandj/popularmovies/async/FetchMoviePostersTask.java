package com.wordpress.omanandj.popularmovies.async;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.network.NetworkUtils;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

/**
 * Created by ojha on 28/11/15.
 */
public class FetchMoviePostersTask extends AsyncTask<MoviesSortOrder, Void, AsyncTaskResult<List<MoviePoster>>>
{
    private static final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

    private final IAsyncTaskResponseHandler<AsyncTaskResult<List<MoviePoster>>> mAsyncTaskResponse;
    private final Context mContext;

    private IMovieDbService movieDbService;
    private boolean fetchFavourites;

    public FetchMoviePostersTask(IAsyncTaskResponseHandler<AsyncTaskResult<List<MoviePoster>>> asyncTaskResponse,
            IMovieDbService movieDbService, Context context, boolean fetchFavourites)
    {
        this.mAsyncTaskResponse = asyncTaskResponse;
        this.mContext = context;
        this.movieDbService = movieDbService;
        this.fetchFavourites = fetchFavourites;
    }

    @Override
    protected AsyncTaskResult<List<MoviePoster>> doInBackground(MoviesSortOrder... params)
    {
        Log.v(LOG_TAG, "Fetching popular popular movies..");

        if (!NetworkUtils.isOnline(mContext) && !fetchFavourites) {

            return new AsyncTaskResult<>("Network Connectivity Not Available. Please try after sometime");
        }
        List<MoviePoster> moviePosters = fetchFavourites ? movieDbService.getMoviePostersFromLocalStore(mContext) : getMoviePosters(params[0]);

        return new AsyncTaskResult<>(moviePosters);
    }

    private List<MoviePoster> getMoviePosters(MoviesSortOrder param)
    {
        MoviesSortOrder moviesSortOrder = param;

        Log.v(LOG_TAG, "MovieDetail will be fetched sorted orde by " + moviesSortOrder.getSortOrder());

        return movieDbService.getMoviePosters(moviesSortOrder, mContext);
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
