package com.wordpress.omanandj.popularmovies.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by ojha on 15/01/16.
 */
public interface MovieMetadataColumns
{
    @DataType (DataType.Type.INTEGER)
    @PrimaryKey
    @AutoIncrement
    String _ID = "_id";

    @DataType (DataType.Type.TEXT)
    @NotNull
    String IMAGE_BASE_URL = "base_url";
}
