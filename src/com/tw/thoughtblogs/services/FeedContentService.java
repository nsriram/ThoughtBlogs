package com.tw.thoughtblogs.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import com.tw.thoughtblogs.RSSReader;
import com.tw.thoughtblogs.model.Blog;
import com.tw.thoughtblogs.model.BlogData;

import java.util.Date;
import java.util.List;

import static com.tw.thoughtblogs.Constants.*;

public class FeedContentService extends Service {
    private Handler mHandler = new Handler();
    private RSSReader rssReader = new RSSReader(FEED_URL);
    private BlogData blogData;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        rssReader = new RSSReader(FEED_URL);
        blogData = new BlogData(this);
        mHandler.postDelayed(contentFetchTask, ONE_MINUTE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(contentFetchTask);
        Toast.makeText(this, "Service onDestroy() ", Toast.LENGTH_LONG).show();
    }

    private Runnable contentFetchTask = new Runnable() {
        public void run() {
            Date lastParsedDate = blogData.lastParsedDate();
            List<Blog> blogs = rssReader.fetchLatestEntries(lastParsedDate);
            storeBlogs(blogs);
            mHandler.postDelayed(contentFetchTask, ONE_MINUTE);
        }
    };

    private void storeBlogs(List<Blog> blogs) {
        if (blogs.size() > 0) {
            blogData.store(blogs);
            Intent intent = new Intent(REFRESH_INTENT);
            sendBroadcast(intent);
        }
    }
}
