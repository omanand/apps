package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import com.wordpress.omanandj.popularmovies.model.MovieTrailer;

/**
 * Created by ojha on 14/01/16.
 */
public interface MovieTrailerColumns
{
    @DataType (DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType (DataType.Type.INTEGER)
    @References (table = MovieDatabase.MOVIES, column = MovieColumns._ID)
    @NotNull
    String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String KEY = MovieTrailer.KEY;

    @DataType(DataType.Type.TEXT)
    @NotNull
    String NAME = MovieTrailer.NAME;

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SITE = MovieTrailer.SITE;

    @DataType(DataType.Type.TEXT)
    @NotNull
    String SIZE = MovieTrailer.SIZE;

    @DataType(DataType.Type.TEXT)
    @NotNull
    String TYPE = MovieTrailer.TYPE;
}
