package com.wordpress.omanandj.popularmovies.api;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.wordpress.omanandj.popularmovies.config.MovieDbConfig;
import com.wordpress.omanandj.popularmovies.model.MovieConfig;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MovieReview;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ojha on 13/12/15.
 */
@Module
public class MovieDbApiModule
{

    @Provides
    @Singleton
    IMovieDbApiEndPoint getMovieDbApiEndPoint()
    {
        Gson gson = providesGson();

        IMovieDbApiEndPoint movieDbApiEndpoint = new Retrofit.Builder().baseUrl(MovieDbConfig.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson)).client(providesHttpClient()).build()
                .create(IMovieDbApiEndPoint.class);
        return movieDbApiEndpoint;
    }

    @NonNull
    @Provides
    @Singleton
    Gson providesGson()
    {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setDateFormat("yyyy-MM-dd")
                .registerTypeAdapter(MovieConfig.class,
                        new RestDeserializer<>(MovieConfig.class, MovieConfig.CONFIG_IMAGES))
                .registerTypeAdapter(new TypeToken<List<MoviePoster>>() {
                }.getType(), new RestDeserializer<>(new TypeToken<List<MoviePoster>>() {
                }.getType().getClass(), MovieConfig.RESULTS)).registerTypeAdapter(new TypeToken<List<MovieTrailer>>() {
                }.getType(), new RestDeserializer<>(new TypeToken<List<MovieTrailer>>() {
                }.getType().getClass(), MovieConfig.RESULTS)).registerTypeAdapter(new TypeToken<List<MovieReview>>() {
                }.getType(), new RestDeserializer<>(new TypeToken<List<MovieReview>>() {
                }.getType().getClass(), MovieConfig.RESULTS)).setPrettyPrinting().create();
    }

    @NonNull
    @Provides
    @Singleton
    OkHttpClient providesHttpClient()
    {
        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);

        return client;
    }

    @NonNull
    @Provides
    @Singleton
    IMovieDbService providesMovieDbService(IMovieDbApiEndPoint movieDbApiService)
    {
        return new MovieDbService(movieDbApiService);
    }
}

class RestDeserializer<T> implements JsonDeserializer<T>
{

    private Class<T> mClass;
    private String mKey;

    public RestDeserializer(Class<T> targetClass, String key)
    {
        mClass = targetClass;
        mKey = key;
    }

    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        JsonElement content = je.getAsJsonObject().get(mKey);

        if (content.isJsonArray()) {
            return new Gson().fromJson(content, type);
        }
        else {
            return new Gson().fromJson(content, type);
        }

    }
}
