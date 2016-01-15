package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;

/**
 * Created by ojha on 13/01/16.
 */
public interface MovieColumns
{
    @DataType (DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String ORIGINAL_TITLE = "title";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String POSTER_PATH = MoviePoster.POSTER_PATH;

    @DataType(DataType.Type.TEXT)
    @NotNull
    String OVERVIEW = MovieDetail.OVERVIEW;

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RELEASE_DATE = MovieDetail.RELEASE_DATE;

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String VOTE_AVERAGE = MovieDetail.VOTE_AVERAGE;

    @DataType(DataType.Type.INTEGER)
    @NotNull
    String RUNTIME = MovieDetail.RUNTIME;

}


