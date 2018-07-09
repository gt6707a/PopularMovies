package com.android.gt6707a.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.gt6707a.popularmovies.utilities.NetworkUtils;

import java.lang.ref.WeakReference;
import java.net.URL;

public class MovieQueryTask extends AsyncTask<MovieQueryTask.SortOption, Void, String> {
    private WeakReference<Context> mContext;

    public MovieQueryTask(@NonNull Context context) {
        mContext = new WeakReference<>(context);
    }

    private String query(SortOption sortOption) {
        String uri = sortOption == SortOption.MostPopular ?
                mContext.get().getString(R.string.MOST_POPULAR_MOVIES_URI) :
                mContext.get().getString(R.string.TOP_RATED_MOVIES_URI);

        try {
            URL url = NetworkUtils.getUrl(mContext.get(), uri);
            if (url != null) {
                return NetworkUtils.fetch(url);
            }
        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected String doInBackground(MovieQueryTask.SortOption... sortOptions) {
        return query(sortOptions[0]);
    }

    public enum SortOption {
        MostPopular,
        TopRated
    }
}
