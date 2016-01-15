package com.wordpress.omanandj.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ojha on 05/01/16.
 */
public class MovieTrailer implements Parcelable
{
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";
    public static final String SIZE = "size";
    public static final String TYPE = "type";

    @Expose
    @SerializedName (MoviePoster.ID)
    private String id;

    @Expose
    @SerializedName (KEY)
    private String key;

    @Expose
    @SerializedName (NAME)
    private String name;

    @Expose
    @SerializedName (SITE)
    private String site;

    @SerializedName (SIZE)
    private String size;

    @Expose
    @SerializedName (TYPE)
    private String type;

    private String movieId;

    public MovieTrailer()
    {
    }

    public String getId()
    {
        return id;
    }

    public String getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }

    public String getSite()
    {
        return site;
    }

    public String getSize()
    {
        return size;
    }

    public String getType()
    {
        return type;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getMovieId()
    {
        return movieId;
    }

    public void setMovieId(String movieId)
    {
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieTrailer that = (MovieTrailer) o;

        if (!id.equals(that.id)) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (site != null ? !site.equals(that.site) : that.site != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("MovieTrailer{");
        sb.append("id='").append(id).append('\'');
        sb.append(", key='").append(key).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", site='").append(site).append('\'');
        sb.append(", size='").append(size).append('\'');
        sb.append(", type='").append(type).append('\'');
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
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(size);
        dest.writeString(site);
        dest.writeString(type);
    }

    protected MovieTrailer(Parcel in)
    {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    public static final Creator<MovieTrailer> CREATOR = new Creator<MovieTrailer>() {
        @Override
        public MovieTrailer createFromParcel(Parcel source)
        {
            return new MovieTrailer(source);
        }

        @Override
        public MovieTrailer[] newArray(int size)
        {
            return new MovieTrailer[size];
        }
    };

}
