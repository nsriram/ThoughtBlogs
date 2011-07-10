package com.tw.thoughtblogs;

import com.tw.thoughtblogs.model.Blog;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RSSReader {
    private String feed;

    public RSSReader(String feed) {
        this.feed = feed;
    }

    public List<Blog> fetchLatestEntries(String lastParsedDate) {
        HttpResponse response = null;
        List<Blog> blogs;
        try {
            URI feedURL = new URI(feed);
            HttpGet get = new HttpGet(feedURL);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            response = httpClient.execute(get);
            blogs = new FeedParser().parse(response.getEntity().getContent(), lastParsedDate);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Blog>();
        }
        Collections.reverse(blogs);
        return blogs;
    }
}
