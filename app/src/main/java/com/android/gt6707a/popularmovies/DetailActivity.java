package com.android.gt6707a.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.gt6707a.popularmovies.database.AppDatabase;
import com.android.gt6707a.popularmovies.entities.Movie;
import com.android.gt6707a.popularmovies.entities.Review;
import com.android.gt6707a.popularmovies.entities.Trailer;
import com.android.gt6707a.popularmovies.utilities.NetworkUtils;
import com.android.gt6707a.popularmovies.utilities.ReviewJsonUtils;
import com.android.gt6707a.popularmovies.utilities.TrailerJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity
    implements TrailersAdapter.TrailersAdapterOnClickHandler
    , ReviewsAdapter.ReviewsAdapterOnClickHandler {

    private Movie mMovie;
    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;
    private Button mFavorite;
    private ProgressBar mProgressBar;
    private boolean isSaved;

    private TrailersAdapter mTrailersAdapter;
    private ReviewsAdapter mReviewsAdapter;

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mTitleTextView = findViewById(R.id.tv_title);
        mPosterImageView = findViewById(R.id.iv_poster);
        mReleaseDateTextView = findViewById(R.id.tv_releaseDate);
        mVoteAverageTextView = findViewById(R.id.tv_voteAverage);
        mOverviewTextView = findViewById(R.id.tv_overview);
        mFavorite = findViewById(R.id.b_favorite);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mTrailersAdapter = new TrailersAdapter(this, this);
        mReviewsAdapter = new ReviewsAdapter(this, this);
        mDb = AppDatabase.getInstance(getApplicationContext());

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClicked();
            }
        });

        LinearLayoutManager trailersLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView mTrailersView = findViewById(R.id.rv_trailers);
        mTrailersView.setLayoutManager(trailersLayoutManager);
        mTrailersView.setAdapter(mTrailersAdapter);

        LinearLayoutManager reviewsLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView mReviewsView = findViewById(R.id.rv_reviews);
        mReviewsView.setLayoutManager(reviewsLayoutManager);
        mReviewsView.setAdapter(mReviewsAdapter);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Movie.class.getCanonicalName())) {
                Movie movie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Movie.class.getCanonicalName());
                updateUI(movie);
            }
        }
    }

    private void updateUI(Movie movie) {
        mMovie = movie;
        mTitleTextView.setText(movie.getTitle());
        mReleaseDateTextView.setText(movie.getReleaseYear());
        mVoteAverageTextView.setText(String.format(Locale.getDefault(), "%1$.1f/10", movie.getVoteAverage()));
        mOverviewTextView.setText(movie.getOverview());
        Picasso.with(this).load(getString(R.string.POSTER_BASE_URI) + movie.getPosterPath())
                .resize(500, 1000).centerInside().into(mPosterImageView);

        QueryMovieTrailersTask queryMovieTrailersTask = new QueryMovieTrailersTask(this);
        queryMovieTrailersTask.execute(movie.getId());

        QueryMovieReviewsTask queryMovieReviewsTask = new QueryMovieReviewsTask(this);
        queryMovieReviewsTask.execute(movie.getId());

        mDb.movieDao().loadById(mMovie.getId()).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                isSaved = movie != null;
                mFavorite.setText(isSaved ? "UNMARK AS FAVORITE" : "MARK AS FAVORITE");
            }
        });
    }

    private void onFavoriteClicked() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isSaved) {
                    mDb.movieDao().delete(mMovie);
                } else {
                    mDb.movieDao().insert(mMovie);
                }
            }
        });
    }

    @Override
    public void onClick(Trailer trailer) {
        Intent playVideo = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("http://www.youtube.com/watch?v=%s", trailer.getKey())));
        //playVideo.setDataAndType(Uri.parse(String.format("http://www.youtube.com/watch?v=%s", trailer.getKey())), "video/mp4");
        if (playVideo.resolveActivity(getPackageManager()) != null) {
            startActivity(playVideo);
        }
    }

    @Override
    public void onClick(Review review) {

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

    private static class QueryMovieReviewsTask extends AsyncTask<Integer, Void, String> {
        private final String mMovieReviewsUrl;
        private final DetailActivity mActivity;

        public QueryMovieReviewsTask(@NonNull DetailActivity activity) {
            mActivity = activity;
            mMovieReviewsUrl = activity.getString(R.string.MOVIE_REVIEWS_URL);
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
                    List<Review> reviews = ReviewJsonUtils.toReviews(response);
                    mActivity.mReviewsAdapter.updateReviews(reviews);
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
                URL url = NetworkUtils.getUrl(String.format(mMovieReviewsUrl, movieId));
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
