package com.wordpress.omanandj.popularmovies.service;

import java.util.List;

import android.content.Context;

import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MovieReview;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;

/**
 * Created by ojha on 28/11/15.
 */
public interface IMovieDbService
{

    List<MoviePoster> getMoviePosters(MoviesSortOrder moviesSortOrder, Context context);

    String getMoviePosterUrl(String posterPath);

    MovieDetail getMovieDetail(String id);

    List<MovieTrailer> getMovieTrailers(String movieId);

    List<MovieReview> getMovieReviews(String movieId);

    List<MoviePoster> getMoviePostersFromLocalStore(Context context);

    MovieDetail getMovieDetailsFromLocalStore(Context context, String movieId);

    List<MovieTrailer> getMovieTrailersFromLocalStore(String movieId, Context context);

    List<MovieReview> getMovieReviewsFromLocalStore(String movieId, Context context);

}
