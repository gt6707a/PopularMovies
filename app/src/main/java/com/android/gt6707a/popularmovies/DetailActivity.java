package com.android.gt6707a.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.gt6707a.popularmovies.entities.Movie;
import com.android.gt6707a.popularmovies.entities.Trailer;
import com.android.gt6707a.popularmovies.utilities.NetworkUtils;
import com.android.gt6707a.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
    implements TrailersAdapter.TrailersAdapterOnClickHandler {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private ProgressBar mProgressBar;

    private TrailersAdapter mTrailersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mTitleTextView = findViewById(R.id.tv_title);
        mPosterImageView = findViewById(R.id.iv_poster);
        mReleaseDateTextView = findViewById(R.id.tv_releaseDate);
        mVoteAverageTextView = findViewById(R.id.tv_voteAverage);
        mOverviewTextView = findViewById(R.id.tv_overview);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mTrailersAdapter = new TrailersAdapter(this, this);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView mTrailersView = findViewById(R.id.rv_trailers);
        mTrailersView.setLayoutManager(layoutManager);
        mTrailersView.setAdapter(mTrailersAdapter);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Movie.class.getCanonicalName())) {
                Movie movie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Movie.class.getCanonicalName());
                updateUI(movie);
            }
        }
    }

    private void updateUI(Movie movie) {
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseYear());
        mVoteAverageTextView.setText(String.format(Locale.getDefault(), "%1$.1f/10", movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        Picasso.with(this).load(getString(R.string.POSTER_BASE_URI) + movie.getPosterPath())
                .resize(500, 1000).centerInside().into(mPosterImageView);

        QueryMovieTrailersTask queryMovieTrailersTask = new QueryMovieTrailersTask(this);
        queryMovieTrailersTask.execute(movie.getId());
    }

    @Override
    public void onClick(Trailer trailer) {

    }


    private void showLoading(boolean isLoading) {
        mProgressBar.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
    }

    private static class QueryMovieTrailersTask extends AsyncTask<Integer, Void, String> {
        private final String mMovieVideosUrl;
        private final DetailActivity mActivity;

        public QueryMovieTrailersTask(@NonNull DetailActivity activity) {
            mActivity = activity;
            mMovieVideosUrl = activity.getString(R.string.MOVIE_VIDEOS_URL);
        }

        @Override
        protected void onPreExecute() {
            mActivity.showLoading(true);
        }

        @Override
        protected String doInBackground(Integer... movieIds) {
            return query(movieIds[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            if (!response.isEmpty()) {
                try {
                    List<Trailer> trailers = TrailerJsonUtils.toTrailers(response);
                    mActivity.mTrailersAdapter.updateTrailers(trailers);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mActivity.showLoading(false);
            } else {
                //mActivity.get().showError();
            }
        }

        private String query(int movieId) {
            try {
                URL url = NetworkUtils.getUrl(String.format(mMovieVideosUrl, movieId));
                if (url != null) {
                    return NetworkUtils.fetch(url);
                }
            } catch (Exception e) {
                /* Server probably invalid */
                e.printStackTrace();
            }
            return "";
        }
    }
}
