package com.wordpress.omanandj.popularmovies;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wordpress.omanandj.popularmovies.adapter.MoviePosterAdapter;
import com.wordpress.omanandj.popularmovies.async.AsyncTaskResult;
import com.wordpress.omanandj.popularmovies.async.FetchMoviePostersTask;
import com.wordpress.omanandj.popularmovies.async.IAsyncTaskResponseHandler;
import com.wordpress.omanandj.popularmovies.model.MoviePoster;
import com.wordpress.omanandj.popularmovies.model.MoviesSortOrder;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        IAsyncTaskResponseHandler<AsyncTaskResult<List<MoviePoster>>>
{
    private static final String LOG_TAG = MoviesActivityFragment.class.getSimpleName();
    private static final String MOVIE_POSTERS = "moviePosters";

    private MoviePosterAdapter mMoviePosterAdapter;

    private List<MoviePoster> moviePosters = new ArrayList<>();

    private ProgressBar mProgressBar;

    @Inject
    IMovieDbService movieDbService;

    public MoviesActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ((PopMovieApp) getActivity().getApplication()).getmMovieDbApiComponent().inject(this);

        setHasOptionsMenu(true);
        // Set Default here PreferenceManager.setDefaultValues(this, R.xml.advanced_preferences, false);
        // http://developer.android.com/guide/topics/ui/settings.html
        // Register this onResume and unregister onPause
        PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);

        // TODO: Add option to control data usage
        /*
         * <activity android:NAME="SettingsActivity" ... > <intent-filter> <action
         * android:NAME="android.intent.action.MANAGE_NETWORK_USAGE" /> <category
         * android:NAME="android.intent.category.DEFAULT" /> </intent-filter> </activity>
         */

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

        mMoviePosterAdapter = new MoviePosterAdapter(getActivity(), movieDbService, moviePosters);
        moviePosterGridView.setAdapter(mMoviePosterAdapter);

        moviePosterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MoviePoster moviePoster = mMoviePosterAdapter.getItem(position);
                /*
                 * Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_TEXT,
                 * moviePoster.getId());
                 * 
                 * startActivity(intent);
                 */
                ((Callback) getActivity()).onItemSelected(moviePoster.getId());
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
        mProgressBar.setIndeterminate(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String orderByMostPopular = getString(R.string.sort_order_most_popular);
        final String orderByHighestRated = getString(R.string.sort_order_highest_rated);
        final String showFavourites = getString(R.string.sort_order_favorites);

        String sortOrderPref = sharedPreferences.getString(getString(R.string.sort_order_prefrence_key),
                orderByMostPopular);
        MoviesSortOrder sortOrder = MoviesSortOrder.MOST_POPULAR;

        boolean isFavourite = false;
        if (sortOrderPref.equals(orderByHighestRated)) {
            sortOrder = MoviesSortOrder.HIGEST_RATED;
        }
        else if (sortOrderPref.equals(showFavourites)) {
            isFavourite = true;
        }

        FetchMoviePostersTask fetchMoviePostersTask = new FetchMoviePostersTask(this, movieDbService,
                this.getContext(), isFavourite);
        fetchMoviePostersTask.execute(sortOrder);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if (isAdded() && getString(R.string.sort_order_prefrence_key).equals(key)) {
            fetchMoviePostersData();
        }
    }

    @Override
    public void processFinish(AsyncTaskResult<List<MoviePoster>> result)
    {
        if (!result.hasError() && result.getResult().size() > 0) {
            mMoviePosterAdapter.clear();
            mMoviePosterAdapter.addAll(result.getResult());
            mProgressBar.setIndeterminate(false);
            ((Callback) getActivity()).onDefaultItem(result.getResult().get(0).getId());
        }
        else {
            // Show Toast message
            if (result.hasError()) {
                Toast.makeText(getContext(), getString(R.string.network_connectivity_error_message), Toast.LENGTH_LONG)
                        .show();
            }
            else {
                Toast.makeText(getContext(), getString(R.string.no_favourite_movie_message), Toast.LENGTH_LONG).show();
            }
        }
    }

    public interface Callback
    {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(String movieId);

        void onDefaultItem(String movieId);
    }
}
