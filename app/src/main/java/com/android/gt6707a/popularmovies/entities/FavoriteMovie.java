package com.android.gt6707a.popularmovies.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class FavoriteMovie {

    @PrimaryKey
    private int id;
    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }

    private String title;
    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public FavoriteMovie(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
