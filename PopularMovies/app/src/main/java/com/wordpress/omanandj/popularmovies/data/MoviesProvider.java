package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import android.net.Uri;

/**
 * Created by ojha on 14/01/16.
 */
@ContentProvider (authority = MoviesProvider.AUTHORITY, database = MovieDatabase.class)
public final class MoviesProvider
{
    public static final String AUTHORITY = "com.wordpress.omanandj.popularmovies.MoviesProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path
    {
        String MOVIES = "movies";
        String MOVIE_TRAILER = "movie_trailer";
        String MOVIE_REVIEW = "movie_reviews";
        String MOVIE_METADATA = "movie_metadata";
    }

    private static Uri buildUri(String... paths)
    {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint (table = MovieDatabase.MOVIES)
    public static class Movies
    {
        @ContentUri (path = Path.MOVIES,
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIES);

        @InexactContentUri (name = "MOVIE_ID",
                path = Path.MOVIES + "/#",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(String id)
        {
            return buildUri(Path.MOVIES, id);
        }
    }

    @TableEndpoint (table = MovieDatabase.MOVIE_TRAILER)
    public static class MovieTrailers
    {
        @ContentUri (path = Path.MOVIE_TRAILER,
                type = "vnd.android.cursor.dir/movie_trailer",
                defaultSort = MovieColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIE_TRAILER);

        @InexactContentUri (name = "MOVIE_ID",
                path = Path.MOVIE_TRAILER + "/#" ,
                type = "vnd.android.cursor.item/movie_trailer",
                whereColumn = MovieTrailerColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(String movieId)
        {
            return buildUri(Path.MOVIE_TRAILER, movieId);
        }
    }

    @TableEndpoint (table = MovieDatabase.MOVIE_REVIEW)
    public static class MovieReviews
    {
        @ContentUri (path = Path.MOVIE_REVIEW,
                type = "vnd.android.cursor.dir/movie_review",
                defaultSort = MovieReviewColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIE_REVIEW);

        @InexactContentUri (name = "MOVIE_ID",
                path = Path.MOVIE_REVIEW + "/#" ,
                type = "vnd.android.cursor.item/movie_review",
                whereColumn = MovieReviewColumns.MOVIE_ID,
                pathSegment = 1)
        public static Uri withId(String movieId)
        {
            return buildUri(Path.MOVIE_REVIEW, movieId);
        }
    }

    @TableEndpoint (table = MovieDatabase.MOVIE_METADATA)
    public static class MovieMetadata
    {
        @ContentUri (path = Path.MOVIE_METADATA,
                type = "vnd.android.cursor.dir/movie_metadata",
                defaultSort = MovieMetadataColumns._ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.MOVIE_METADATA);



    }


}
