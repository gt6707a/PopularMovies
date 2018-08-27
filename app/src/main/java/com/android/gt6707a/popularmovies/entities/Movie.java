package com.android.gt6707a.popularmovies.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Movie implements Serializable{
    @PrimaryKey
    private int id;
    private long voteCount;
    private String title;
    private String posterPath;
    private String overview;
    private double voteAverage;
    private String releaseDate;

    @Ignore
    public Movie() {}

    public Movie(int id, long voteCount, String title, String posterPath, String overview, double voteAverage, String releaseDate) {
        this.id = id;
        this.voteCount = voteCount;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }
    public Movie setId(int id) {
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
