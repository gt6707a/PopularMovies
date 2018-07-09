package com.android.gt6707a.popularmovies.utilities;

import com.android.gt6707a.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieDbJsonUtils {
    private static String VOTE_COUNT = "vote_count";
    private static String ID = "id";
    public static String VIDEO = "video";
    private static String VOTE_AVERAGE = "vote_average";
    private static String TITLE = "title";
    private static String POSTER_PATH = "poster_path";
    private static String OVERVIEW = "overview";
    private static String RELEASE_DATE = "release_date";

    public static List<Movie> toMovies(String json)
        throws JSONException {

        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");

        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < results.length(); i++) {
            JSONObject movieJson = results.getJSONObject(i);
            long id = movieJson.getLong(ID);
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
