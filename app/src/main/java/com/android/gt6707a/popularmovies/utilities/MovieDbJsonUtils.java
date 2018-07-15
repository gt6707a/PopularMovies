package com.android.gt6707a.popularmovies.utilities;

import com.android.gt6707a.popularmovies.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieDbJsonUtils {
    private final static String VOTE_COUNT = "vote_count";
    private final static String ID = "id";
    private final static String VIDEO = "video";
    private final static String VOTE_AVERAGE = "vote_average";
    private final static String TITLE = "title";
    private final static String POSTER_PATH = "poster_path";
    private final static String OVERVIEW = "overview";
    private final static String RELEASE_DATE = "release_date";

    public static List<Movie> toMovies(String json)
        throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            int id = movieJson.getInt(ID);
            long voteCount = movieJson.getLong(VOTE_COUNT);
            String title = movieJson.getString(TITLE);
            String posterPath = movieJson.getString(POSTER_PATH);
            String overview = movieJson.getString(OVERVIEW);
            double voteAverage = movieJson.getDouble(VOTE_AVERAGE);
            String releaseDate = movieJson.getString(RELEASE_DATE);

            Movie movie = new Movie()
                    .setId(id)
                    .setTitle(title)
                    .setPosterPath(posterPath)
                    .setVoteCount(voteCount)
                    .setOverview(overview)
                    .setReleaseDate(releaseDate)
                    .setVoteAverage(voteAverage);

            movies.add(movie);
        }

        return movies;
    }
}
