package com.wordpress.omanandj.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wordpress.omanandj.popularmovies.async.FetchMovieDetailTask;
import com.wordpress.omanandj.popularmovies.async.IAsyncTaskResponse;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.service.impl.MovieDbService;

import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements IAsyncTaskResponse<MovieDetail>
{

    private static final String LOG_TAG = MoviesActivityFragment.class.getSimpleName();
    private static final String DURATION_FORMAT = "%dminutes";
    private static final String MOVIE_RATING_FORMAT = "%s / 10";
    private static final String MOVIE_DETAIL_KEY = "movieDetail";

    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView movieYearTextView;
    private TextView movieDurationTextView;
    private TextView movieRatingTextView;
    private TextView movieDetailDescTextView;

    private MovieDetail movieDetail;

    public MovieDetailActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(MOVIE_DETAIL_KEY, movieDetail);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView =  inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (null == savedInstanceState  || !savedInstanceState.containsKey(MOVIE_DETAIL_KEY)) {
            Intent intent = getActivity().getIntent();
            if (null !=intent  && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String movieId = intent.getStringExtra(Intent.EXTRA_TEXT);
                FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(this);
                fetchMovieDetailTask.execute(movieId);
            }

        } else {
            movieDetail = savedInstanceState.getParcelable(MOVIE_DETAIL_KEY);

        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieTitleTextView = (TextView) getView().findViewById(R.id.movie_detail_title_id);
        moviePosterImageView = (ImageView) getView().findViewById(R.id.movie_poster_image_view_id);
        movieYearTextView = (TextView) getView().findViewById(R.id.movie_detail_year_id);
        movieDurationTextView = (TextView) getView().findViewById(R.id.movie_detail_runtime_id);
        movieRatingTextView = (TextView) getView().findViewById(R.id.movie_detail_vote_avg_id);
        movieDetailDescTextView = (TextView) getView().findViewById(R.id.movie_detail_desc_id);

        if(null != movieDetail) {
            refreshUi(movieDetail);
        }
    }

    @Override
    public void processFinish(MovieDetail movieDetail)
    {
        this.movieDetail = movieDetail;

        refreshUi(movieDetail);
    }

    private void refreshUi(MovieDetail movieDetail)
    {

        IMovieDbService movieDbService = MovieDbService.getInstance();

        int year = getYear(movieDetail.getReleaseDate());

        String moviePosterUrl = movieDbService.getMoviePosterUrl(movieDetail.getPosterPath());

        movieTitleTextView.setText(movieDetail.getOriginalTitle());

        Picasso.with(getActivity().getBaseContext()).load(moviePosterUrl).into(moviePosterImageView);

        movieYearTextView.setText(Integer.toString(year));

        movieDurationTextView.setText(String.format(DURATION_FORMAT, movieDetail.getRuntime()));

        movieRatingTextView.setText(String.format(MOVIE_RATING_FORMAT, movieDetail.getVoteAverage()));

        movieDetailDescTextView.setText(movieDetail.getOverview());
    }

    private int getYear(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
