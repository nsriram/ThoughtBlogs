package com.tw.thoughtblogs.model;

public class Blog {
    private int id;
    private String title;
    private String origLink;
    private String pubDate;
    private String description;
    private int status;

    public String getTitle() {
        return title;
    }

    public String getOrigLink() {
        return origLink;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public Blog(int id, String title, String origLink, String pubDate, String description, int status) {
        this(title, origLink, pubDate, description, status);
        this.id = id;
    }

    public Blog(String title, String origLink, String pubDate, String description, int status) {
        this.title = title;
        this.origLink = origLink;
        this.pubDate = pubDate;
        this.description = description;
        this.status = status;
    }
}
