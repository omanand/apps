package com.wordpress.omanandj.popularmovies.api;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import com.wordpress.omanandj.popularmovies.model.MovieConfig;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MovieReview;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;

/**
 * Created by ojha on 13/12/15.
 */
public interface IMovieDbApiEndPoint
{
    String DISCOVER_MOVIE_URL_PATH = "3/discover/movie";
    String SORT_BY = "sort_by";
    String MOVIEDB_API_KEY = "api_key";
    String MOVIE_URL_PATH = "3/movie";
    String CONFIGURATION_URL_PATH = "3/configuration";

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET (CONFIGURATION_URL_PATH)
    Call<MovieConfig> getImageBaseUrl(@Query (MOVIEDB_API_KEY) String apiKey);

    @GET (DISCOVER_MOVIE_URL_PATH)
    Call<List<MoviePoster>> getMoviePosters(@Query (MOVIEDB_API_KEY) String apiKey,
            @Query (SORT_BY) String moviesSortOrder);

    @GET (MOVIE_URL_PATH + "/{id}")
    Call<MovieDetail> getMovieDetail(@Path ("id") String Id, @Query (MOVIEDB_API_KEY) String apiKey);

    @GET (MOVIE_URL_PATH + "/{id}/videos")
    Call<List<MovieTrailer>> getMovieTrailers(@Path ("id") String movieId, @Query (MOVIEDB_API_KEY) String apiKey);

    @GET (MOVIE_URL_PATH + "/{id}/reviews")
    Call<List<MovieReview>> getMovieReviews(@Path ("id") String movieId, @Query (MOVIEDB_API_KEY) String apiKey);

}
