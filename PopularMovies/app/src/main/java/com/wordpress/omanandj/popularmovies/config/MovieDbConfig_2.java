package com.wordpress.omanandj.popularmovies.config;

import com.wordpress.omanandj.popularmovies.BuildConfig;

/**
 * Created by ojha on 28/11/15.
 */
public class MovieDbConfig
{
    public static String getApiKey()
    {
        return BuildConfig.THE_MOVIE_DB_API_KEY;
    }

    public static String getBaseUrl()
    {
        return BuildConfig.THE_MOVIE_DB_BASE_URL;
    }
}
