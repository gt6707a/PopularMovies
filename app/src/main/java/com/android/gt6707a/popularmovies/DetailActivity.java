package com.android.gt6707a.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mReleaseDateTextView;
    private TextView mVoteAverageTextView;
    private TextView mOverviewTextView;

    private Movie mMovie;

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
                mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra(Movie.class.getCanonicalName());
                mTitleTextView.setText(mMovie.getTitle());
                mReleaseDateTextView.setText(mMovie.getReleaseYear());
                mVoteAverageTextView.setText(String.format("%1$.1f/10", mMovie.getVoteAverage()));
                mOverviewTextView.setText(mMovie.getOverview());
                Picasso.with(this).load(getString(R.string.POSTER_BASE_URI) + mMovie.getPosterPath())
                        .resize(500, 1000).centerInside().into(mPosterImageView);
            }
        }
    }
}
