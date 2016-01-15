package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by ojha on 14/01/16.
 */
@Database (version = MovieDatabase.VERSION)
public final class MovieDatabase
{
    public static final int VERSION = 2;

    private MovieDatabase()
    {
    }

    @Table (MovieColumns.class)
    public static final String MOVIES = "movies";

    @Table (MovieTrailerColumns.class)
    public static final String MOVIE_TRAILER = "movie_trailer";

    @Table (MovieReviewColumns.class)
    public static final String MOVIE_REVIEW = "movie_review";

    @Table (MovieMetadataColumns.class)
    public static final String MOVIE_METADATA = "movie_metadata";
}
