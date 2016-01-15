package com.wordpress.omanandj.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;

/**
 * Created by ojha on 06/01/16.
 */
public class MovieReview implements Parcelable
{
    @Expose
    private String id;

    @Expose
    private String author;

    @Expose
    private String content;

    @Expose
    private String url;

    private String movieId;

    public void setId(String id)
    {
        this.id = id;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    protected MovieReview(Parcel in)
    {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    public MovieReview()
    {
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in)
        {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size)
        {
            return new MovieReview[size];
        }
    };

    public String getId()
    {
        return id;
    }

    public String getAuthor()
    {
        return author;
    }

    public String getContent()
    {
        return content;
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieReview that = (MovieReview) o;

        if (!id.equals(that.id)) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        return url.equals(that.url);

    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + url.hashCode();
        return result;
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
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("MovieReview{");
        sb.append("id='").append(id).append('\'');
        sb.append(", AUTHOR='").append(author).append('\'');
        sb.append(", CONTENT='").append(content).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
