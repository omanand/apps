package com.wordpress.omanandj.popularmovies.config;

import javax.inject.Singleton;

import com.wordpress.omanandj.popularmovies.MovieDetailActivityFragment;
import com.wordpress.omanandj.popularmovies.MoviesActivityFragment;
import com.wordpress.omanandj.popularmovies.api.MovieDbApiModule;

import dagger.Component;

/**
 * Created by ojha on 16/12/15.
 */
@Singleton
@Component (modules = MovieDbApiModule.class)
public interface MovieDbApiComponent
{
    void inject(MovieDetailActivityFragment movieDetailActivityFragment);

    void inject(MoviesActivityFragment moviesActivityFragment);
}
