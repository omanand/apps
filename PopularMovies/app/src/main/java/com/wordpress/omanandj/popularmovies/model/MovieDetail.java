package com.wordpress.omanandj.popularmovies.model;

import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ojha on 28/11/15.
 */
public class MovieDetail implements Parcelable
{
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String RUNTIME = "runtime";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";

    @Expose
    @SerializedName (MoviePoster.ID)
    private String id;

    @Expose
    @SerializedName (MoviePoster.ORIGINAL_TITLE)
    private String originalTitle;

    @Expose
    @SerializedName (MoviePoster.POSTER_PATH)
    private String posterPath;

    @Expose
    @SerializedName (OVERVIEW)
    private String overview;

    @Expose
    @SerializedName (RELEASE_DATE)
    private Date releaseDate;

    @Expose
    @SerializedName (VOTE_AVERAGE)
    private String voteAverage;

    @Expose
    @SerializedName (RUNTIME)
    private int runtime;

    private List<MovieTrailer> movieTrailers;

    private List<MovieReview> movieReviews;

    private boolean isFavourite;

    public MovieDetail()
    {
    }

    public List<MovieTrailer> getMovieTrailers()
    {
        return movieTrailers;
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailers)
    {
        this.movieTrailers = movieTrailers;
    }

    public List<MovieReview> getMovieReviews()
    {
        return movieReviews;
    }

    public void setMovieReviews(List<MovieReview> movieReviews)
    {
        this.movieReviews = movieReviews;
    }

    public boolean isFavourite()
    {
        return isFavourite;
    }

    public void setFavourite(boolean favourite)
    {
        isFavourite = favourite;
    }

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

    public String getOverview()
    {
        return overview;
    }

    public Date getReleaseDate()
    {
        return releaseDate;
    }

    public String getVoteAverage()
    {
        return voteAverage;
    }

    public int getRuntime()
    {
        return runtime;
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

    public void setOverview(String overview)
    {
        this.overview = overview;
    }

    public void setReleaseDate(Date releaseDate)
    {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(String voteAverage)
    {
        this.voteAverage = voteAverage;
    }

    public void setRuntime(int runtime)
    {
        this.runtime = runtime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MovieDetail movieDetail = (MovieDetail) o;

        if (runtime != movieDetail.runtime) return false;
        if (!id.equals(movieDetail.id)) return false;
        if (!originalTitle.equals(movieDetail.originalTitle)) return false;
        if (!posterPath.equals(movieDetail.posterPath)) return false;
        return releaseDate.equals(movieDetail.releaseDate);

    }

    @Override
    public int hashCode()
    {
        int result = id.hashCode();
        result = 31 * result + originalTitle.hashCode();
        result = 31 * result + posterPath.hashCode();
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + runtime;
        return result;
    }

    protected MovieDetail(Parcel in)
    {
        id = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = new Date(in.readLong());
        voteAverage = in.readString();
        runtime = in.readInt();
    }

    public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in)
        {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size)
        {
            return new MovieDetail[size];
        }
    };

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
        dest.writeString(overview);
        dest.writeLong(releaseDate.getTime());
        dest.writeString(voteAverage);
        dest.writeInt(runtime);
    }
}
