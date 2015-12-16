package com.wordpress.omanandj.popularmovies;

import android.app.Application;

import com.wordpress.omanandj.popularmovies.config.DaggerMovieDbApiComponent;
import com.wordpress.omanandj.popularmovies.config.MovieDbApiComponent;

/**
 * Created by ojha on 16/12/15.
 */
public class PopMovieApp extends Application
{
    private MovieDbApiComponent mMovieDbApiComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mMovieDbApiComponent = DaggerMovieDbApiComponent.create();
    }

    public MovieDbApiComponent getmMovieDbApiComponent()
    {
        return mMovieDbApiComponent;
    }
}
