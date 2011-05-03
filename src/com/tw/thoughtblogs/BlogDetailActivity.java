package com.tw.thoughtblogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class BlogDetailActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_detail);

        Intent launchingIntent = getIntent();
        String content = launchingIntent.getData().toString();

        WebView viewer = (WebView) findViewById(R.id.blogDetailView);
        viewer.loadUrl(content);
    }
}
