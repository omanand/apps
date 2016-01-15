package com.wordpress.omanandj.popularmovies.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ojha on 13/12/15.
 */
public class MovieConfig
{
    public static final String CONFIG_IMAGES = "images";
    public static final String SECURE_BASE_URL = "secure_base_url";
    public static final String LOG_TAG = MovieConfig.class.getSimpleName();
    public static final String RESULTS = "results";

    @SerializedName (SECURE_BASE_URL)
    private String secureBaseUrl;

    public MovieConfig()
    {
    }

    public String getSecureBaseUrl()
    {
        return secureBaseUrl;
    }

}
