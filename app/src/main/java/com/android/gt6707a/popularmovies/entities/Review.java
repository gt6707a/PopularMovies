package com.android.gt6707a.popularmovies.entities;

public class Review {
    private String id;
    public String getId() { return id; }
    public Review setId(String id) {
        this.id = id;
        return this;
    }

    private String author;
    public String getAuthor() { return author; }
    public Review setAuthor(String author) {
        this.author = author;
        return this;
    }

    private String content;
    public String getContent() { return content; }
    public Review setContent(String content) {
        this.content = content;
        return this;
    }
}
