package com.tw.thoughtblogs.model;

public class Blog {
    private String title;
    private String origLink;
    private String pubDate;

    public String getTitle() {
        return title;
    }

    public String getOrigLink() {
        return origLink;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Blog(String title, String origLink, String pubDate) {
        this.title = title;
        this.origLink = origLink;
        this.pubDate = pubDate;
    }
}
