package com.wordpress.omanandj.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.wordpress.omanandj.popularmovies.adapter.MoviePosterAdapter;
import com.wordpress.omanandj.popularmovies.async.FetchMoviePostersTask;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String LOG_TAG = MoviesActivityFragment.class.getSimpleName();
    private static final String MOVIE_POSTERS = "moviePosters";

    private MoviePosterAdapter mMoviePosterAdapter;

    private List<MoviePoster> moviePosters = new ArrayList<>();

    private ProgressBar mProgressBar;

    public MoviesActivityFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelableArrayList(MOVIE_POSTERS, (ArrayList<? extends Parcelable>) moviePosters);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_id);

        GridView moviePosterGridView = (GridView) rootView.findViewById(R.id.movieposter_gridview);

        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), moviePosters);
        moviePosterGridView.setAdapter(mMoviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MoviePoster moviePoster = mMoviePosterAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT,
                        moviePoster.getId());

                startActivity(intent);
            }
        });

        if (null == savedInstanceState || !savedInstanceState.containsKey(MOVIE_POSTERS)) {
            fetchMoviePostersData();
        }
        else {
            List<MoviePoster> tempMoviePosters = savedInstanceState.getParcelableArrayList(MOVIE_POSTERS);
            moviePosters.clear();
            mMoviePosterAdapter.addAll(tempMoviePosters);
            mMoviePosterAdapter.notifyDataSetChanged();
        }

        return rootView;
    }

    private void fetchMoviePostersData()
    {
        Log.v(LOG_TAG, "Fetching movie poster datat");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String orderByMostPopular = getString(R.string.sort_order_most_popular);
        final String orderByHighestRated = getString(R.string.sort_order_highest_rated);

        String sortOrderPref = sharedPreferences.getString(getString(R.string.sort_order_prefrence_key),
                orderByMostPopular);
        MoviesSortOrder sortOrder = MoviesSortOrder.MOST_POPULAR;

        if (sortOrderPref.equals(orderByHighestRated)) {
            sortOrder = MoviesSortOrder.HIGEST_RATED;
        }

        FetchMoviePostersTask fetchMoviePostersTask = new FetchMoviePostersTask(mMoviePosterAdapter, mProgressBar);
        fetchMoviePostersTask.execute(sortOrder);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (isAdded() && getString(R.string.sort_order_prefrence_key).equals(key)) {
            fetchMoviePostersData();
        }
    }
}
