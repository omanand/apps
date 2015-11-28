package com.wordpress.omanandj.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by ojha on 28/11/15.
 *
 * Sort object for popular movies homepage.
 */
public class MoviePoster implements Parcelable
{

    private String id;
    private String originalTitle;
    private String posterPath;

    public MoviePoster(String id, String originalTitle, String posterPath)
    {
        this.id = id;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
    }

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

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("MoviePoster{");
        sb.append("id='").append(id).append('\'');
        sb.append(", originalTitle='").append(originalTitle).append('\'');
        sb.append(", posterPath='").append(posterPath).append('\'');
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
