package com.android.gt6707a.popularmovies;

import java.io.Serializable;

public class Movie implements Serializable{
    private long id;
    private long voteCount;
    private boolean video;
    private String title;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    public long getId() {
        return id;
    }
    public Movie setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }
    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }
    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public long getVoteCount() {
        return voteCount;
    }
    public Movie setVoteCount(long voteCount) {
        this.voteCount = voteCount;
        return this;
    }

    public String getOverview() { return overview; }
    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public double getVoteAverage() { return voteAverage; }
    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getReleaseYear() {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return "";
        }

        return releaseDate.substring(0, releaseDate.indexOf('-'));
    }
}
