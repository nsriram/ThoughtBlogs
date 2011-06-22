package com.tw.thoughtblogs;

import com.tw.thoughtblogs.model.Blog;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RSSReader {
    private String feed;
    private FeedParser feedParser;

    public RSSReader(String feed) {
        this.feed = feed;
        this.feedParser = new FeedParser();
    }

    public List<Blog> fetchLatestEntries(Date lastParsedDate) {
        HttpResponse response = null;
        List<Blog> blogs = new ArrayList<Blog>();
        try {
            URI feedURL = new URI(feed);
            HttpGet get = new HttpGet(feedURL);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            response = httpClient.execute(get);
            blogs = feedParser.parse(response.getEntity().getContent(), lastParsedDate);
        } catch (Exception e) {
            return new ArrayList<Blog>();
        }
        Collections.reverse(blogs);
        return blogs;
    }
}
