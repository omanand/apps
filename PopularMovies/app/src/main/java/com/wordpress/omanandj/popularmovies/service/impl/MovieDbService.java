package com.wordpress.omanandj.popularmovies.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.config.MovieDbConfig;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.network.HttpRequestHelper;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

/**
 * Created by ojha on 28/11/15.
 */
public class MovieDbService implements IMovieDbService
{

    private static final String LOG_TAG = MovieDbService.class.getSimpleName();

    private static final String DISCOVER_MOVIE_URL_PATH = "/discover/movie";
    private static final String CONFIGURATION_URL_PATH = "/configuration";
    private static final String MOVIE_URL_PATH = "/movie";
    private static final String MOVIEDB_API_KEY = "api_key";
    private static final String CONFIG_IMAGES = "images";
    private static final String CONFIG_IMAGES_BASE_URL = "secure_base_url";
    private static final String RESULTS = "results";
    private static final String ID = "id";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String RUNTIME = "runtime";
    private static final String SORT_BY = "sort_by";

    private static MovieDbService _movieDbService;
    private static final Object lock = new Object();

    private String imageSecureBaseUrl;

    private MovieDbService()
    {
        imageSecureBaseUrl = getImageSecureBaseUrl();
    }

    public static MovieDbService getInstance()
    {
        if (null == _movieDbService) {
            synchronized (lock) {
                if (null == _movieDbService) {
                    _movieDbService = new MovieDbService();
                }
            }
        }
        return _movieDbService;
    }

    private String getImageSecureBaseUrl()
    {
        final String movieDbConfigUrl = MovieDbConfig.getBaseUrl() + CONFIGURATION_URL_PATH;

        final Uri configUri = Uri.parse(movieDbConfigUrl).buildUpon()
                .appendQueryParameter(MOVIEDB_API_KEY, MovieDbConfig.getApiKey()).build();

        try {
            final String responseJsonStr = HttpRequestHelper.executeSecureGet(configUri);

            return parseMovieDbConfigResponse(responseJsonStr);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting configuration from the movie db", e);
            return null;
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing configuration json for the movie db");
            return null;
        }
    }

    private String parseMovieDbConfigResponse(String jsonString) throws JSONException
    {

        JSONObject jsonConfigResponse = new JSONObject(jsonString);
        JSONObject jsonImages = jsonConfigResponse.getJSONObject(CONFIG_IMAGES);

        return jsonImages.getString(CONFIG_IMAGES_BASE_URL);

    }

    private List<MoviePoster> parseDiscoveryResponse(String response) throws JSONException
    {
        List<MoviePoster> posters = new ArrayList<>();

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray jsonMoviesArray = jsonResponse.getJSONArray(RESULTS);

        for (int i = 0; i < jsonMoviesArray.length(); i++) {
            JSONObject jsonMovie = jsonMoviesArray.getJSONObject(i);

            MoviePoster poster = new MoviePoster(jsonMovie.getString(ID), jsonMovie.getString(ORIGINAL_TITLE),
                    jsonMovie.getString(POSTER_PATH));

            posters.add(poster);
        }

        Log.v(LOG_TAG, "Posters size = " + posters.size());
        Log.v(LOG_TAG, "Posters = " + posters);
        return posters;
    }

    @Override
    public List<MoviePoster> getMoviePosters(MoviesSortOrder moviesSortOrder)
    {
        final String movieDbPostersUrl = MovieDbConfig.getBaseUrl() + DISCOVER_MOVIE_URL_PATH;

        final Uri configUri = Uri.parse(movieDbPostersUrl).buildUpon()
                .appendQueryParameter(SORT_BY, moviesSortOrder.getSortOrder())
                .appendQueryParameter(MOVIEDB_API_KEY, MovieDbConfig.getApiKey()).build();

        try {
            final String responseJsonStr = HttpRequestHelper.executeSecureGet(configUri);

            return parseDiscoveryResponse(responseJsonStr);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting doscovery from the movie db", e);
            return null;
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing doscovery json for the movie db", e);
            return null;
        }

    }

    @Override
    public String getMoviePosterUrl(String posterPath)
    {
        return imageSecureBaseUrl + "w185" + posterPath;
    }

    @Override
    public MovieDetail getMovieDetail(String id)
    {
        final String movieUrl = MovieDbConfig.getBaseUrl() + MOVIE_URL_PATH;

        final Uri configUri = Uri.parse(movieUrl).buildUpon()
                .appendPath(id)
                .appendQueryParameter(MOVIEDB_API_KEY, MovieDbConfig.getApiKey()).build();

        try {
            final String responseJsonStr = HttpRequestHelper.executeSecureGet(configUri);

            return parseMovieDetailResponse(responseJsonStr);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting movie details from the movie db", e);
            return null;
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "Error in parsing movie details json for the movie db", e);
            return null;
        }
    }

    private MovieDetail parseMovieDetailResponse(String responseJsonStr) throws JSONException
    {

        JSONObject jsonMovie = new JSONObject(responseJsonStr);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        Date releaseDate;
        try {
            releaseDate = dateFormatter.parse(jsonMovie.getString(RELEASE_DATE));
        }
        catch (ParseException e) {
            Log.e(LOG_TAG, "Error in parsing date");
            releaseDate = new Date();
        }
        return new MovieDetail(jsonMovie.getString(ID), jsonMovie.getString(ORIGINAL_TITLE),
                jsonMovie.getString(POSTER_PATH), jsonMovie.getString(OVERVIEW), releaseDate,
                jsonMovie.getString(VOTE_AVERAGE), jsonMovie.getInt(RUNTIME)

        );

    }
}
