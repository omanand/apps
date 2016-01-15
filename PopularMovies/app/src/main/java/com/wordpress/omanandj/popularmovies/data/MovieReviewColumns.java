package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

/**
 * Created by ojha on 14/01/16.
 */
public interface MovieReviewColumns
{

    @DataType (DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType (DataType.Type.INTEGER)
    @References (table = MovieDatabase.MOVIES, column = MovieColumns._ID)
    @NotNull
    String MOVIE_ID = "movie_id";

    @DataType (DataType.Type.TEXT)
    @NotNull
    String AUTHOR = "author";

    @DataType (DataType.Type.TEXT)
    @NotNull
    String CONTENT = "content";

    @DataType (DataType.Type.TEXT)
    @NotNull
    String URL = "url";
}
