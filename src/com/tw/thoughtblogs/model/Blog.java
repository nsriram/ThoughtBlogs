package com.tw.thoughtblogs.model;

public class Blog {
    private String title;
    private String origLink;
    private String pubDate;
    private String description;

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

    public Blog(String title, String origLink, String pubDate, String description) {
        this.title = title;
        this.origLink = origLink;
        this.pubDate = pubDate;
        this.description = description;
    }
}
