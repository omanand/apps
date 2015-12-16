package com.wordpress.omanandj.popularmovies.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import android.os.Looper;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.api.IMovieDbApiEndPoint;
import com.wordpress.omanandj.popularmovies.config.MovieDbConfig;
import com.wordpress.omanandj.popularmovies.model.MovieConfig;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ojha on 28/11/15.
 *
 */
public class MovieDbService implements IMovieDbService
{

    private static final String LOG_TAG = MovieDbService.class.getSimpleName();

    private String imageSecureBaseUrl;

    private IMovieDbApiEndPoint movieDbApiEndPoint;

    public MovieDbService(IMovieDbApiEndPoint movieDbApiEndPoint)
    {
        this.movieDbApiEndPoint = movieDbApiEndPoint;
    }


    private synchronized void initilizeConfiguration()
    {
        if (null != imageSecureBaseUrl) {
            return;
        }

        final Call<MovieConfig> movieConfigCall = movieDbApiEndPoint.getImageBaseUrl(MovieDbConfig.getApiKey());

        if (Looper.myLooper() != Looper.getMainLooper()) {
            try {
                final Response<MovieConfig> response = movieConfigCall.execute();

                if (response.isSuccess()) {
                    imageSecureBaseUrl = response.body().getSecureBaseUrl();
                    Log.v(LOG_TAG, "Image Base URL is " + imageSecureBaseUrl);

                }
                else {
                    Log.e(LOG_TAG, "Error in getting configuration from the movie db");
                    Log.e(LOG_TAG, "Error " + response.errorBody().string());
                }
            }
            catch (IOException e) {
                Log.e(LOG_TAG, "Error in getting configuration from the movie db", e);
            }
        }
        else {
            movieConfigCall.enqueue(new Callback<MovieConfig>() {
                @Override
                public void onResponse(Response<MovieConfig> response, Retrofit retrofit)
                {
                    imageSecureBaseUrl = response.body().getSecureBaseUrl();
                }

                @Override
                public void onFailure(Throwable t)
                {
                    Log.e(LOG_TAG, "Error in getting configuration from the movie db", t);
                }
            });
        }

        return;
    }

    @Override
    public List<MoviePoster> getMoviePosters(MoviesSortOrder moviesSortOrder)
    {
        initilizeConfiguration();
        List<MoviePoster> moviePosters = Collections.emptyList();
        try {
            final Call<List<MoviePoster>> moviePosterCall = movieDbApiEndPoint.getMoviePosters(
                    MovieDbConfig.getApiKey(), moviesSortOrder);

            final Response<List<MoviePoster>> response = moviePosterCall.execute();
            if (response.isSuccess()) {
                moviePosters = response.body();
            }
            else {
                Log.e(LOG_TAG, "Error in discoverying movies from the movie db");
                Log.e(LOG_TAG, "Error " + response.errorBody().string());
            }

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in discoverying movies from the movie db", e);
        }
        return moviePosters;

    }

    @Override
    public String getMoviePosterUrl(String posterPath)
    {
        return imageSecureBaseUrl + "w185" + posterPath;
    }

    @Override
    public MovieDetail getMovieDetail(String id)
    {
        MovieDetail movieDetail = null;

        try {
            final Call<MovieDetail> movieDetailCall = movieDbApiEndPoint.getMovieDetail(id, MovieDbConfig.getApiKey());

            final Response<MovieDetail> response = movieDetailCall.execute();
            if (response.isSuccess()) {
                movieDetail = response.body();
            }
            else {
                Log.e(LOG_TAG, "Error in getting movie details from the movie db");
                Log.e(LOG_TAG, "Error " + response.errorBody().string());
            }

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in discoverying movies from the movie db", e);
        }
        return movieDetail;

    }

}
