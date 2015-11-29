package com.wordpress.omanandj.popularmovies.async;

import android.text.TextUtils;

/**
 * Created by ojha on 29/11/15.
 */
public class AsyncTaskResult<T>
{
    private T result;
    private String error;

    public AsyncTaskResult(T result)
    {
        this.result = result;
    }

    public AsyncTaskResult(String error)
    {
        this.error = error;
    }

    public T getResult()
    {
        return result;
    }

    public String getError()
    {
        return error;
    }

    public boolean hasError()
    {
        return !TextUtils.isEmpty(error);
    }
}
