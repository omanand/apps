package com.wordpress.omanandj.popularmovies.async;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.data.MovieColumns;
import com.wordpress.omanandj.popularmovies.data.MoviesProvider;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;
import com.wordpress.omanandj.popularmovies.network.NetworkUtils;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

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
        AsyncTaskResult<MovieDetail> result;

        String movieId = params[0];

        Log.v(LOG_TAG, "MovieDetail will be fetched for id " + movieId);

        if (!NetworkUtils.isOnline(mContext)) {
            // Try to get it from the local db.
            MovieDetail movieDetail = movieDbService.getMovieDetailsFromLocalStore(mContext, movieId);
            if (null != movieDetail) {
                result = new AsyncTaskResult<>(movieDetail);
            }
            else {
                result = new AsyncTaskResult<>("Network Connectivity Not Available. Please try after sometime");
            }
        }
        else {

            MovieDetail movieDetail = getMovieDetails(movieId);
            result = new AsyncTaskResult<>(movieDetail);
        }
        return result;
    }

    @NonNull
    private MovieDetail getMovieDetails(String movieId)
    {
        MovieDetail movieDetail = movieDbService.getMovieDetail(movieId);
        List<MovieTrailer> movieTrailers = movieDbService.getMovieTrailers(movieId);
        movieDetail.setMovieTrailers(movieTrailers);

        movieDetail.setMovieReviews(movieDbService.getMovieReviews(movieId));

        boolean isFavourite = isFavouriteMovie(movieId);
        movieDetail.setFavourite(isFavourite);

        return movieDetail;
    }

    private boolean isFavouriteMovie(String movieId)
    {
        boolean isFavourite = false;
        Cursor cursor = mContext.getContentResolver().query(MoviesProvider.Movies.withId(movieId),
                new String[] { MovieColumns._ID }, null, null, null);

        if (null != cursor) {
            isFavourite = cursor.getCount() == 1;
            cursor.close();
        }
        return isFavourite;
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<MovieDetail> result)
    {
        asyncTaskResponse.processFinish(result);

    }
}
