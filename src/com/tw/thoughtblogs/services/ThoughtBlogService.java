package com.tw.thoughtblogs.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.tw.thoughtblogs.BlogListActivity;
import com.tw.thoughtblogs.R;
import com.tw.thoughtblogs.RSSReader;
import com.tw.thoughtblogs.model.Blog;
import com.tw.thoughtblogs.model.BlogData;

import java.util.List;

import static com.tw.thoughtblogs.util.Constants.FEED_URL;

public class ThoughtBlogService extends Service implements Runnable {
    private Handler mHandler = new Handler();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mHandler.postDelayed(this, 600000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(this);
    }


    @Override
    public void run() {
        BlogData blogData = new BlogData(getContext());
        String lastParsedDate = blogData.lastParsedDate();
        blogData.close();
        RSSReader rssReader = new RSSReader(FEED_URL);
        List<Blog> blogs = rssReader.fetchLatestEntries(lastParsedDate);
        storeBlogs(blogs);
        mHandler.removeCallbacks(this);
        mHandler.postDelayed(this, 7200000);
    }

    private Context getContext() {
        return this;
    }

    private void storeBlogs(List<Blog> blogs) {
        if (blogs.size() > 0) {
            BlogData blogData = new BlogData(this);
            blogData.store(blogs);
            blogData.close();
            notifyStatusBar(blogs.size());
        }
    }

    private void notifyStatusBar(int size) {
        String ns = Context.NOTIFICATION_SERVICE;
        int icon = R.drawable.icon;
        CharSequence tickerText = " New ThoughtBlogs";
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, tickerText, when);
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        Context context = getApplicationContext();
        CharSequence contentText = size + " new entries posted on ThoughtBlogs.";
        Intent notificationIntent = new Intent(this, BlogListActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, tickerText, contentText, contentIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        mNotificationManager.notify(1, notification);
    }
}
