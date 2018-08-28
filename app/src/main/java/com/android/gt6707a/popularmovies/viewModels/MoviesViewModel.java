package com.android.gt6707a.popularmovies.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.android.gt6707a.popularmovies.AppExecutors;
import com.android.gt6707a.popularmovies.database.AppDatabase;
import com.android.gt6707a.popularmovies.entities.Movie;
import com.android.gt6707a.popularmovies.utilities.MovieDbJsonUtils;
import com.android.gt6707a.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MoviesViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> isBusy;
    public LiveData<Boolean> getIsBusy() {
        return isBusy;
    }

    private final MutableLiveData<List<Movie>> data = new MutableLiveData<>();
    public LiveData<List<Movie>> getMovies() {
        return data;
    }

    private final LiveData<List<Movie>> favorites;
    public LiveData<List<Movie>> getFavorites() {
        return favorites;
    }

    public MoviesViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        favorites = database.movieDao().loadAll();
        isBusy = new MutableLiveData<>();
    }

    public void fetch(final QueryOptions queryOption) {
        isBusy.setValue(true);
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = query(queryOption);
                    final List<Movie> movies = MovieDbJsonUtils.toMovies(response);
                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            data.setValue(movies);
                            isBusy.setValue(false);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String query(QueryOptions queryOption) {
        String uri = queryOption == QueryOptions.MostPopular ?
                "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc" :
                "https://api.themoviedb.org/3/movie/top_rated";

        try {
            URL url = NetworkUtils.getUrl(uri);
            if (url != null) {
                return NetworkUtils.fetch(url);
            }
        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
        return "";
    }

    public enum QueryOptions {
        MostPopular,
        TopRated,
        Favorites
    }
}
