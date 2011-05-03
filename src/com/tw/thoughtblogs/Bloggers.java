package com.tw.thoughtblogs;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleCursorAdapter;
import com.tw.thoughtblogs.model.BlogData;
import com.tw.thoughtblogs.services.FeedContentService;

import static com.tw.thoughtblogs.Constants.FROM;
import static com.tw.thoughtblogs.Constants.TO;

public class Bloggers extends ListActivity {

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
        Log.v("Bloggers ", intent.toString());
        if (Constants.REFRESH_INTENT.equals(intent.getAction())) {
            setListContent();
        }
    }

    private void setListContent() {
        Cursor cursor = blogData.load();
        startManagingCursor(cursor);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, FROM, TO);
        this.setListAdapter(adapter);
    }

    private void startFeedContentService() {
        Intent intent = new Intent(Bloggers.this, FeedContentService.class);
        startService(intent);
    }

    private void setBlogData() {
        if (blogData == null) {
            this.blogData = new BlogData(this);
        }
    }
}
