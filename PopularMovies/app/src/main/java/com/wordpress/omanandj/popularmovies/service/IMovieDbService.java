package com.wordpress.omanandj.popularmovies.service;

import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;

import java.util.List;

/**
 * Created by ojha on 28/11/15.
 */
public interface IMovieDbService
{

    List<MoviePoster> getMoviePosters(MoviesSortOrder moviesSortOrder);

    String getMoviePosterUrl(String posterPath);

    MovieDetail getMovieDetail(String id);
}
