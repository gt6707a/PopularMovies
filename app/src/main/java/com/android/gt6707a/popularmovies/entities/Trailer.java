package com.android.gt6707a.popularmovies.entities;

public class Trailer {
    private String id;
    public String getId() {
        return id;
    }
    public Trailer setId(String id) {
        this.id = id;
        return this;
    }

    private String name;
    public String getName() {
        return name;
    }
    public Trailer setName(String name) {
        this.name = name;
        return this;
    }

    private String site;
    public String getSite() { return site; }
    public Trailer setSite(String site) {
        this.site = site;
        return this;
    }

    private String key;
    public String getKey() { return key; }
    public Trailer setKey(String key) {
        this.key = key;
        return this;
    }
}
