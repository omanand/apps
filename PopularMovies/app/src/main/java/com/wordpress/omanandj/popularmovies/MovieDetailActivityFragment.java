package com.wordpress.omanandj.popularmovies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.wordpress.omanandj.popularmovies.adapter.MovieReviewAdapter;
import com.wordpress.omanandj.popularmovies.adapter.MovieTrailerAdapter;
import com.wordpress.omanandj.popularmovies.async.AsyncTaskResult;
import com.wordpress.omanandj.popularmovies.async.FetchMovieDetailTask;
import com.wordpress.omanandj.popularmovies.async.IAsyncTaskResponseHandler;
import com.wordpress.omanandj.popularmovies.data.MovieColumns;
import com.wordpress.omanandj.popularmovies.data.MovieReviewColumns;
import com.wordpress.omanandj.popularmovies.data.MovieTrailerColumns;
import com.wordpress.omanandj.popularmovies.data.MoviesProvider;
import com.wordpress.omanandj.popularmovies.model.MovieDetail;
import com.wordpress.omanandj.popularmovies.model.MovieReview;
import com.wordpress.omanandj.popularmovies.model.MovieTrailer;
import com.wordpress.omanandj.popularmovies.service.IMovieDbService;
import com.wordpress.omanandj.popularmovies.utils.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements
        IAsyncTaskResponseHandler<AsyncTaskResult<MovieDetail>>
{

    private static final String LOG_TAG = MoviesActivityFragment.class.getSimpleName();
    private static final String DURATION_FORMAT = "%dminutes";
    private static final String MOVIE_RATING_FORMAT = "%s / 10";
    private static final String MOVIE_DETAIL_KEY = "movieDetail";
    public static final String MOVIE_TRAILERS_KEY = "movieTrailers";
    public static final String MOVIE_REVIEWS_KEY = "movieReviews";
    public static final String MOVIE_ID = "movieId";

    private TextView movieTitleTextView;
    private ImageView moviePosterImageView;
    private TextView movieYearTextView;
    private TextView movieDurationTextView;
    private TextView movieRatingTextView;
    private TextView movieDetailDescTextView;
    private TextView markAsFavouriteButtom;
    private TextView mTrailersHeaderTextView;
    private TextView mReviewsHeaderTextView;

    private MovieDetail movieDetail;
    private List<MovieTrailer> movieTrailers = new ArrayList<>();
    private MovieTrailerAdapter mMovieTrailerAdapter;

    private List<MovieReview> movieReviews = new ArrayList<>();
    private MovieReviewAdapter mMovieReviewAdapter;

    private String mMovieId;

    private ShareActionProvider mShareActionProvider;

    @Inject
    IMovieDbService movieDbService;

    public MovieDetailActivityFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        ((PopMovieApp) getActivity().getApplication()).getmMovieDbApiComponent().inject(this);

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(MOVIE_DETAIL_KEY, movieDetail);
        outState.putParcelableArrayList(MOVIE_TRAILERS_KEY, (ArrayList<? extends Parcelable>) movieTrailers);
        outState.putParcelableArrayList(MOVIE_REVIEWS_KEY, (ArrayList<? extends Parcelable>) movieReviews);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.movie_detail_fragment_menu, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_menu_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider. You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (null != mShareActionProvider && movieDetail != null && null != movieDetail.getMovieTrailers()
                && movieDetail.getMovieTrailers().size() > 0) {
            mShareActionProvider.setShareIntent(createShareForecastIntent(Utils.getYouTubeLink(movieDetail
                    .getMovieTrailers().get(0).getKey())));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovieId = arguments.getString(MOVIE_ID);
        }
        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mMovieTrailerAdapter = new MovieTrailerAdapter(getActivity(), movieTrailers);
        ListView movieTrailersListView = (ListView) rootView.findViewById(R.id.listview_movie_trailers);
        movieTrailersListView.setAdapter(mMovieTrailerAdapter);

        movieTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                MovieTrailer movieTrailer = mMovieTrailerAdapter.getItem(position);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Utils.getYouTubeLink(movieTrailer.getKey())));
                startActivity(intent);
            }
        });

        mMovieReviewAdapter = new MovieReviewAdapter(getActivity(), movieReviews);
        ListView movieReviewsListView = (ListView) rootView.findViewById(R.id.listview_movie_reviews);
        movieReviewsListView.setAdapter(mMovieReviewAdapter);

        if (null == savedInstanceState || !savedInstanceState.containsKey(MOVIE_DETAIL_KEY)) {

            if (null != mMovieId) {
                FetchMovieDetailTask fetchMovieDetailTask = new FetchMovieDetailTask(this, movieDbService, getContext());
                fetchMovieDetailTask.execute(mMovieId);
            }

        }
        else {
            movieDetail = savedInstanceState.getParcelable(MOVIE_DETAIL_KEY);
            List<MovieTrailer> tempMovieTrailers = savedInstanceState.getParcelableArrayList(MOVIE_TRAILERS_KEY);
            refreshMovieTrailerAdapter(tempMovieTrailers);

            List<MovieReview> tempMovieReviews = savedInstanceState.getParcelableArrayList(MOVIE_REVIEWS_KEY);
            refreshMovieReviewAdapter(tempMovieReviews);
        }

        return rootView;
    }

    private void refreshMovieReviewAdapter(List<MovieReview> tempMovieReviews)
    {
        movieReviews.clear();
        mMovieReviewAdapter.addAll(tempMovieReviews);
        mMovieReviewAdapter.notifyDataSetChanged();
    }

    private void refreshMovieTrailerAdapter(List<MovieTrailer> tempMovieTrailers)
    {
        movieTrailers.clear();
        mMovieTrailerAdapter.addAll(tempMovieTrailers);
        mMovieTrailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        movieTitleTextView = (TextView) getView().findViewById(R.id.movie_detail_title_id);
        moviePosterImageView = (ImageView) getView().findViewById(R.id.movie_poster_image_view_id);
        movieYearTextView = (TextView) getView().findViewById(R.id.movie_detail_year_id);
        movieDurationTextView = (TextView) getView().findViewById(R.id.movie_detail_runtime_id);
        movieRatingTextView = (TextView) getView().findViewById(R.id.movie_detail_vote_avg_id);
        movieDetailDescTextView = (TextView) getView().findViewById(R.id.movie_detail_desc_id);
        markAsFavouriteButtom = (TextView) getView().findViewById(R.id.mark_favourite_id);
        mTrailersHeaderTextView = (TextView) getView().findViewById(R.id.trailers_header_id);
        mReviewsHeaderTextView = (TextView) getView().findViewById(R.id.reviews_header_id);

        markAsFavouriteButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (null != movieDetail) {
                    if (movieDetail.isFavourite()) {
                        // removeFavou
                        getContext().getContentResolver().delete(
                                MoviesProvider.MovieReviews.withId(movieDetail.getId()), null, null);
                        getContext().getContentResolver().delete(
                                MoviesProvider.MovieTrailers.withId(movieDetail.getId()), null, null);
                        getContext().getContentResolver().delete(MoviesProvider.Movies.withId(movieDetail.getId()),
                                null, null);
                        movieDetail.setFavourite(false);

                        v.setBackgroundResource(R.drawable.favourite_background_unselected);

                        Toast.makeText(getContext(), getString(R.string.removed_from_favourite), Toast.LENGTH_SHORT)
                                .show();
                    }
                    else {
                        addToFavouriteMovie();
                        movieDetail.setFavourite(true);
                        v.setBackgroundResource(R.drawable.favourite_background_selected);
                        Toast.makeText(getContext(), getString(R.string.mark_as_favourite), Toast.LENGTH_SHORT).show();
                    }
                    // MoviesActivityFragment fragment = (MoviesActivityFragment)
                    // getActivity().getSupportFragmentManager().
                }
            }
        });
        if (null != movieDetail) {
            refreshUi(movieDetail);
        }
    }

    private void addToFavouriteMovie()
    {
        ContentValues values = new ContentValues();
        values.put(MovieColumns._ID, movieDetail.getId());
        values.put(MovieColumns.ORIGINAL_TITLE, movieDetail.getOriginalTitle());
        values.put(MovieColumns.OVERVIEW, movieDetail.getOverview());
        values.put(MovieColumns.POSTER_PATH, movieDetail.getPosterPath());
        values.put(MovieColumns.RELEASE_DATE, movieDetail.getReleaseDate().getTime());
        values.put(MovieColumns.RUNTIME, movieDetail.getRuntime());
        values.put(MovieColumns.VOTE_AVERAGE, movieDetail.getVoteAverage());
        getContext().getContentResolver().insert(MoviesProvider.Movies.withId(movieDetail.getId()), values);
        saveMovieReviews();
        saveMovieTrailers();
    }

    private void saveMovieTrailers()
    {
        List<MovieTrailer> movieTrailers = movieDetail.getMovieTrailers();
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movieTrailers.size());

        for (MovieTrailer movieTrailer : movieTrailers) {
            ContentProviderOperation.Builder builder = ContentProviderOperation
                    .newInsert(MoviesProvider.MovieTrailers.CONTENT_URI);
            builder.withValue(MovieTrailerColumns.MOVIE_ID, movieDetail.getId());
            builder.withValue(MovieTrailerColumns.KEY, movieTrailer.getKey());
            builder.withValue(MovieTrailerColumns.NAME, movieTrailer.getName());
            builder.withValue(MovieTrailerColumns.SITE, movieTrailer.getSite());
            builder.withValue(MovieTrailerColumns.SIZE, movieTrailer.getSize());
            builder.withValue(MovieTrailerColumns.TYPE, movieTrailer.getType());
            batchOperations.add(builder.build());
        }
        try {
            getContext().getContentResolver().applyBatch(MoviesProvider.AUTHORITY, batchOperations);
        }
        catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    private void saveMovieReviews()
    {
        List<MovieReview> movieReviews = movieDetail.getMovieReviews();
        ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movieReviews.size());

        for (MovieReview movieReview : movieReviews) {
            ContentProviderOperation.Builder builder = ContentProviderOperation
                    .newInsert(MoviesProvider.MovieReviews.CONTENT_URI);
            builder.withValue(MovieReviewColumns.MOVIE_ID, movieDetail.getId());
            builder.withValue(MovieReviewColumns.AUTHOR, movieReview.getAuthor());
            builder.withValue(MovieReviewColumns.CONTENT, movieReview.getContent());
            builder.withValue(MovieReviewColumns.URL, movieReview.getUrl());
            batchOperations.add(builder.build());
        }
        try {
            getContext().getContentResolver().applyBatch(MoviesProvider.AUTHORITY, batchOperations);
        }
        catch (RemoteException | OperationApplicationException e) {
            Log.e(LOG_TAG, "Error applying batch insert", e);
        }
    }

    @Override
    public void processFinish(AsyncTaskResult<MovieDetail> result)
    {
        if (!result.hasError()) {
            this.movieDetail = result.getResult();

            refreshMovieTrailerAdapter(movieDetail.getMovieTrailers());
            refreshMovieReviewAdapter(movieDetail.getMovieReviews());
            refreshUi(movieDetail);
        }
        else {
            Toast.makeText(getContext(), getString(R.string.network_connectivity_error_message), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void refreshUi(MovieDetail movieDetail)
    {

        int year = getYear(movieDetail.getReleaseDate());

        String moviePosterUrl = movieDbService.getMoviePosterUrl(movieDetail.getPosterPath());

        movieTitleTextView.setText(movieDetail.getOriginalTitle());

        Picasso.with(getActivity()).load(moviePosterUrl).into(moviePosterImageView);

        movieYearTextView.setText(Integer.toString(year));

        movieDurationTextView.setText(String.format(DURATION_FORMAT, movieDetail.getRuntime()));

        movieRatingTextView.setText(String.format(MOVIE_RATING_FORMAT, movieDetail.getVoteAverage()));

        movieDetailDescTextView.setText(movieDetail.getOverview());
        markAsFavouriteButtom.setVisibility(View.VISIBLE);

        mTrailersHeaderTextView.setVisibility(View.VISIBLE);
        mReviewsHeaderTextView.setVisibility(View.VISIBLE);
        if (movieDetail.isFavourite()) {
            markAsFavouriteButtom.setBackgroundResource(R.drawable.favourite_background_selected);
        }
        if (null != mShareActionProvider && movieDetail != null && null != movieDetail.getMovieTrailers()
                && movieDetail.getMovieTrailers().size() > 0) {
            mShareActionProvider.setShareIntent(createShareForecastIntent(Utils.getYouTubeLink(movieDetail
                    .getMovieTrailers().get(0).getKey())));
        }

    }

    private int getYear(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    private Intent createShareForecastIntent(String videoLink)
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, videoLink);
        return shareIntent;
    }

}
