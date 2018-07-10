package com.android.gt6707a.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

        mTitleTextView = findViewById(R.id.tv_title);
        mPosterImageView = findViewById(R.id.iv_poster);
        mReleaseDateTextView = findViewById(R.id.tv_releaseDate);
        mVoteAverageTextView = findViewById(R.id.tv_voteAverage);
        mOverviewTextView = findViewById(R.id.tv_overview);

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
    }
}
