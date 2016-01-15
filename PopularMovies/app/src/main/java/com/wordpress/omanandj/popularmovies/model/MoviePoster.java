package com.wordpress.omanandj.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojha on 28/11/15.
 *
 * Sort object for popular movies homepage.
 */
public class MoviePoster implements Parcelable
{

    public static final String ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String POSTER_PATH = "poster_path";
    public static final String LOG_TAG = MoviePoster.class.getSimpleName();

    @SerializedName (ID)
    private String id;

    @SerializedName (ORIGINAL_TITLE)
    private String originalTitle;

    @SerializedName (POSTER_PATH)
    private String posterPath;

    public MoviePoster()
    {
    }

    /*
     * public MoviePoster(String id, String ORIGINAL_TITLE, String POSTER_PATH) { this.id = id; this.ORIGINAL_TITLE =
     * ORIGINAL_TITLE; this.POSTER_PATH = POSTER_PATH; }
     */

    protected MoviePoster(Parcel in)
    {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<MoviePoster> CREATOR = new Creator<MoviePoster>() {
        @Override
        public MoviePoster createFromParcel(Parcel in)
        {
            return new MoviePoster(in);
        }

        @Override
        public MoviePoster[] newArray(int size)
        {
            return new MoviePoster[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public String getOriginalTitle()
    {
        return originalTitle;
    }

    public String getPosterPath()
    {
        return posterPath;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle)
    {
        this.originalTitle = originalTitle;
    }

    public void setPosterPath(String posterPath)
    {
        this.posterPath = posterPath;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("MoviePoster{");
        sb.append("id='").append(id).append('\'');
        sb.append(", ORIGINAL_TITLE='").append(originalTitle).append('\'');
        sb.append(", POSTER_PATH='").append(posterPath).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(id);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoviePoster that = (MoviePoster) o;

        if (!id.equals(that.id)) return false;
        if (!originalTitle.equals(that.originalTitle)) return false;
        return posterPath.equals(that.posterPath);

    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + originalTitle.hashCode();
        result = 31 * result + posterPath.hashCode();
        return result;
    }
}
