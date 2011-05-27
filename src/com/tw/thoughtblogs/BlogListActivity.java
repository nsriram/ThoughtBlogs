package com.tw.thoughtblogs;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.tw.thoughtblogs.model.Blog;
import com.tw.thoughtblogs.model.BlogData;
import com.tw.thoughtblogs.services.ThoughtBlogService;
import com.tw.thoughtblogs.util.Constants;

import java.util.List;

public class BlogListActivity extends ListActivity {
    private final BlogData blogData = new BlogData(this);

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.REFRESH_INTENT.equals(intent.getAction())) {
                setListContent();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startFeedContentService();
        setListContent();
        handleIntent(getIntent());

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.REFRESH_INTENT);
        this.registerReceiver(this.receiver, filter);
    }

    private void handleIntent(Intent intent) {
        if (Constants.REFRESH_INTENT.equals(intent.getAction())) {
            setListContent();
        }
    }

    private void setListContent() {
        List<Blog> blogs = blogData.list();
        this.setListAdapter(new BlogAdapter(this, R.layout.list_item, blogs));
    }

    private void startFeedContentService() {
        Intent intent = new Intent(BlogListActivity.this, ThoughtBlogService.class);
        startService(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView blogIdTextView = (TextView) v.findViewById(R.id.blog_id);
        blogIdTextView.setTypeface(null, Typeface.NORMAL);
        blogIdTextView.setTextColor(-3355444);
        String blogId = blogIdTextView.getText().toString();
        blogData.markRead(blogId);
        Intent showContent = new Intent(getApplicationContext(), BlogDetailActivity.class);
        showContent.setData(Uri.parse(blogId));
        startActivity(showContent);
    }
}
