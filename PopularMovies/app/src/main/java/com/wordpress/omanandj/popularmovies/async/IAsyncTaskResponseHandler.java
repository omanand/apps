package com.wordpress.omanandj.popularmovies.async;

/**
 * Created by ojha on 29/11/15.
 */
public interface IAsyncTaskResponseHandler<T>
{
    void processFinish(T result);
}
