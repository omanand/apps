package com.wordpress.omanandj.popularmovies.api;

import com.wordpress.omanandj.popularmovies.model.MovieConfig;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

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
                                           @Query (SORT_BY) MoviesSortOrder moviesSortOrder);

    @GET (MOVIE_URL_PATH + "/{id}")
    Call<MovieDetail> getMovieDetail(@Path ("id") String Id, @Query (MOVIEDB_API_KEY) String apiKey);

}
