package com.tw.thoughtblogs;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.tw.thoughtblogs.model.BlogData;
import com.tw.thoughtblogs.services.ThoughtBlogService;
import com.tw.thoughtblogs.util.Constants;

import static com.tw.thoughtblogs.util.Constants.FROM;
import static com.tw.thoughtblogs.util.Constants.TO;

public class BlogListActivity extends ListActivity {

    private BlogData blogData;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleIntent(intent);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startFeedContentService();
        setBlogData();
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
        Cursor cursor = blogData.load();
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, R.layout.list_item, cursor, FROM, TO);
        this.setListAdapter(adapter);
    }

    private void startFeedContentService() {
        Intent intent = new Intent(BlogListActivity.this, ThoughtBlogService.class);
        startService(intent);
    }

    private void setBlogData() {
        if (blogData == null) {
            this.blogData = new BlogData(this);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView blogIdTextView = (TextView) v.findViewById(R.id.blog_id);
        String blogId = blogIdTextView.getText().toString();
        blogData.markRead(blogId);
        Intent showContent = new Intent(getApplicationContext(), BlogDetailActivity.class);
        showContent.setData(Uri.parse(blogId));
        startActivity(showContent);
    }
}
