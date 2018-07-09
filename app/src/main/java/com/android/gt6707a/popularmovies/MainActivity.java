package com.android.gt6707a.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.gt6707a.popularmovies.utilities.MovieDbJsonUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
    MovieAdapter.MovieAdapterOnClickHandler
{
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessage;
    private MovieQueryTask.SortOption mCurrentSortOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, getResources().getInteger(R.integer.NUMBER_OF_COLUMNS), GridLayoutManager.VERTICAL, false);
        mMovieAdapter = new MovieAdapter(this, this);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mRecyclerView = findViewById(R.id.recyclerview_movies);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);

        mCurrentSortOption = MovieQueryTask.SortOption.MostPopular;
        refreshData();
    }

    @Override
    public void onClick(Movie movie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(movie.getClass().getCanonicalName(), movie);
        startActivity(intentToStartDetailActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.settings, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortId = item.getItemId();
        if (sortId == R.id.sort_popular) {
            mCurrentSortOption = MovieQueryTask.SortOption.MostPopular;
        } else if (sortId == R.id.sort_top_rated) {
            mCurrentSortOption = MovieQueryTask.SortOption.TopRated;
        }

        refreshData();
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        MovieQueryTask movieQueryTask = new MovieQueryTask(this) {
            @Override
            protected void onPreExecute() {
                showLoading(true);
            }

            @Override
            protected void onPostExecute(String response) {
                if (!response.isEmpty()) {
                    try {
                        List<Movie> movies = MovieDbJsonUtils.toMovies(response);
                        mMovieAdapter.updateMovies(movies);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showLoading(false);
                } else {
                    showError();
                }
            }
        };

        movieQueryTask.execute(mCurrentSortOption);
    }

    private void showLoading(boolean isLoading) {

        mRecyclerView.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        mLoadingIndicator.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
}
