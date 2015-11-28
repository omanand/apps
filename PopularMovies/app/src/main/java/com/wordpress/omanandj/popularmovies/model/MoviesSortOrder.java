package com.wordpress.omanandj.popularmovies.model;

/**
 * Created by ojha on 28/11/15.
 */
public enum MoviesSortOrder
{
    MOST_POPULAR("popularity.desc"),
    HIGEST_RATED("vote_average.desc");

    private String sortOrder;

    MoviesSortOrder(String sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public String getSortOrder()
    {
        return sortOrder;
    }
}
