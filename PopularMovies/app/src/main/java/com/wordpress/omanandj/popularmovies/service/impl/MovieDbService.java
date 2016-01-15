package com.wordpress.omanandj.popularmovies.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;

import com.wordpress.omanandj.popularmovies.api.IMovieDbApiEndPoint;
import com.wordpress.omanandj.popularmovies.config.MovieDbConfig;
import com.wordpress.omanandj.popularmovies.data.MovieColumns;
import com.wordpress.omanandj.popularmovies.data.MovieMetadataColumns;
import com.wordpress.omanandj.popularmovies.data.MovieReviewColumns;
import com.wordpress.omanandj.popularmovies.data.MovieTrailerColumns;
import com.wordpress.omanandj.popularmovies.data.MoviesProvider;
import com.wordpress.omanandj.popularmovies.model.MovieConfig;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MovieReview;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.network.NetworkUtils;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

/**
 * Created by ojha on 28/11/15.
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

    private synchronized void initConfiguration(Context context)
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
        if (null != imageSecureBaseUrl && null == getImageBaseUrlFromLocalStore(context)) {
            ContentValues values = new ContentValues();
            values.put(MovieMetadataColumns.IMAGE_BASE_URL, imageSecureBaseUrl);
            context.getContentResolver().insert(MoviesProvider.MovieMetadata.CONTENT_URI, values);
        }

    }

    @Override
    public List<MoviePoster> getMoviePosters(MoviesSortOrder moviesSortOrder, Context context)
    {
        initConfiguration(context, false);
        List<MoviePoster> moviePosters = Collections.emptyList();
        try {
            final Call<List<MoviePoster>> moviePosterCall = movieDbApiEndPoint.getMoviePosters(
                    MovieDbConfig.getApiKey(), moviesSortOrder.getSortOrder());

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

    @Override
    public List<MovieTrailer> getMovieTrailers(String movieId)
    {
        List<MovieTrailer> movieTrailers = Collections.emptyList();
        try {
            final Call<List<MovieTrailer>> movieTrailersCall = movieDbApiEndPoint.getMovieTrailers(movieId,
                    MovieDbConfig.getApiKey());

            final Response<List<MovieTrailer>> response = movieTrailersCall.execute();
            if (response.isSuccess()) {
                movieTrailers = response.body();
            }
            else {
                Log.e(LOG_TAG, "Error in getting movie trailers from the movie db for movie id " + movieId);
                Log.e(LOG_TAG, "Error " + response.errorBody().string());
            }

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting movie trailers from the movie db for id " + movieId, e);
        }
        return movieTrailers;
    }

    @Override
    public List<MovieReview> getMovieReviews(String movieId)
    {
        List<MovieReview> movieReviews = Collections.emptyList();
        try {
            final Call<List<MovieReview>> movieTrailersCall = movieDbApiEndPoint.getMovieReviews(movieId,
                    MovieDbConfig.getApiKey());

            final Response<List<MovieReview>> response = movieTrailersCall.execute();
            if (response.isSuccess()) {
                movieReviews = response.body();
            }
            else {
                Log.e(LOG_TAG, "Error in getting movie reviews from the movie db for id " + movieId);
                Log.e(LOG_TAG, "Error " + response.errorBody().string());
            }

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in getting movie reviews from the movie db for id " + movieId, e);
        }
        return movieReviews;
    }

    @Override
    public List<MoviePoster> getMoviePostersFromLocalStore(Context context)
    {
        initConfiguration(context, true);
        List<MoviePoster> moviePosters = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(MoviesProvider.Movies.CONTENT_URI,
                new String[] { MovieColumns._ID, MovieColumns.ORIGINAL_TITLE, MovieColumns.POSTER_PATH }, null, null,
                null);

        if (null != cursor) {

            while (cursor.moveToNext()) {
                MoviePoster moviePoster = new MoviePoster();
                moviePoster.setId(cursor.getString(cursor.getColumnIndex(MovieColumns._ID)));
                moviePoster.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
                moviePoster.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH)));
                moviePosters.add(moviePoster);
            }
            cursor.close();
        }

        return moviePosters;

    }

    @Override
    public MovieDetail getMovieDetailsFromLocalStore(Context context, String movieId)
    {
        Cursor cursor = context.getContentResolver()
                .query(MoviesProvider.Movies.withId(movieId),
                        new String[] { MovieColumns._ID, MovieColumns.ORIGINAL_TITLE, MovieColumns.POSTER_PATH,
                            MovieColumns.OVERVIEW, MovieColumns.RELEASE_DATE, MovieColumns.RUNTIME,
                            MovieColumns.VOTE_AVERAGE }, null, null, null);

        MovieDetail movieDetail = null;

        if (null != cursor && cursor.getCount() == 1) {

            while (cursor.moveToNext()) {
                movieDetail = new MovieDetail();
                movieDetail.setId(cursor.getString(cursor.getColumnIndex(MovieColumns._ID)));
                movieDetail.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieColumns.ORIGINAL_TITLE)));
                movieDetail.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieColumns.POSTER_PATH)));
                movieDetail.setOverview(cursor.getString(cursor.getColumnIndex(MovieColumns.OVERVIEW)));
                movieDetail.setReleaseDate(new Date(cursor.getLong(cursor.getColumnIndex(MovieColumns.RELEASE_DATE))));
                movieDetail.setRuntime(cursor.getInt(cursor.getColumnIndex(MovieColumns.RUNTIME)));
                movieDetail.setVoteAverage(cursor.getString(cursor.getColumnIndex(MovieColumns.VOTE_AVERAGE)));
                movieDetail.setFavourite(true);

                movieDetail.setMovieTrailers(getMovieTrailersFromLocalStore(movieDetail.getId(), context));
                movieDetail.setMovieReviews(getMovieReviewsFromLocalStore(movieDetail.getId(), context));

            }
            cursor.close();
        }

        return movieDetail;

    }

    @Override
    public List<MovieTrailer> getMovieTrailersFromLocalStore(String movieId, Context context)
    {
        Cursor cursor = context.getContentResolver().query(
                MoviesProvider.MovieTrailers.withId(movieId),
                new String[] { MovieTrailerColumns._ID, MovieTrailerColumns.MOVIE_ID, MovieTrailerColumns.KEY,
                    MovieTrailerColumns.NAME, MovieTrailerColumns.SITE, MovieTrailerColumns.SIZE,
                    MovieTrailerColumns.TYPE }, null, null, null);

        List<MovieTrailer> movieTrailers = new ArrayList<>();

        if (null != cursor) {

            while (cursor.moveToNext()) {
                MovieTrailer movieTrailer = new MovieTrailer();
                movieTrailer.setId(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns._ID)));
                movieTrailer.setMovieId(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.MOVIE_ID)));
                movieTrailer.setKey(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.KEY)));
                movieTrailer.setName(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.NAME)));
                movieTrailer.setSize(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.SIZE)));
                movieTrailer.setSite(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.SITE)));
                movieTrailer.setType(cursor.getString(cursor.getColumnIndex(MovieTrailerColumns.TYPE)));

                movieTrailers.add(movieTrailer);
            }
            cursor.close();
        }

        return movieTrailers;
    }

    @Override
    public List<MovieReview> getMovieReviewsFromLocalStore(String movieId, Context context)
    {
        Cursor cursor = context.getContentResolver().query(
                MoviesProvider.MovieReviews.withId(movieId),
                new String[] { MovieReviewColumns._ID, MovieReviewColumns.MOVIE_ID, MovieReviewColumns.AUTHOR,
                    MovieReviewColumns.CONTENT, MovieReviewColumns.URL }, null, null, null);

        List<MovieReview> movieReviews = new ArrayList<>();

        if (null != cursor) {

            while (cursor.moveToNext()) {
                MovieReview movieReview = new MovieReview();
                movieReview.setId(cursor.getString(cursor.getColumnIndex(MovieReviewColumns._ID)));
                movieReview.setMovieId(cursor.getString(cursor.getColumnIndex(MovieReviewColumns.MOVIE_ID)));
                movieReview.setAuthor(cursor.getString(cursor.getColumnIndex(MovieReviewColumns.AUTHOR)));
                movieReview.setContent(cursor.getString(cursor.getColumnIndex(MovieReviewColumns.CONTENT)));
                movieReview.setUrl(cursor.getString(cursor.getColumnIndex(MovieReviewColumns.URL)));

                movieReviews.add(movieReview);
            }
            cursor.close();
        }

        return movieReviews;
    }

    private void initConfiguration(Context context, boolean fallbackToLocalStore)
    {
        if (NetworkUtils.isOnline(context)) {
            initConfiguration(context);
        }
        else if (fallbackToLocalStore) {
            imageSecureBaseUrl = getImageBaseUrlFromLocalStore(context);
        }
    }

    private String getImageBaseUrlFromLocalStore(Context context)
    {
        String url = null;
        Cursor cursor = context.getContentResolver().query(MoviesProvider.MovieMetadata.CONTENT_URI,
                new String[] { MovieMetadataColumns.IMAGE_BASE_URL }, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            url = cursor.getString(cursor.getColumnIndex(MovieMetadataColumns.IMAGE_BASE_URL));
        }
        return url;
    }
}
