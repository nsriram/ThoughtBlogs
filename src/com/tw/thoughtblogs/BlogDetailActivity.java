package com.tw.thoughtblogs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.tw.thoughtblogs.model.Blog;
import com.tw.thoughtblogs.model.BlogData;

public class BlogDetailActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blog_detail);
        Intent launchingIntent = getIntent();
        String blogID = launchingIntent.getData().toString();
        BlogData blogData = new BlogData(this);
        Blog blog = blogData.loadDescription(blogID);
        blogData.close();
        String details = blog.getDescription();
        details = Uri.encode(details);
        details = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-16le\"><body>" + details + "</body></html>";
        TextView header = (TextView) findViewById(R.id.detail_view_title);
        header.setText(blog.getTitle());
        header.setTextColor(Color.WHITE);
        WebView viewer = (WebView) findViewById(R.id.blogDetailView);
        viewer.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl(imageJS());
            }
        });
        viewer.loadData(details, "text/html", "utf-8");
    }

    private String imageJS() {
        return "javascript:(function () {\n" +
                "  var w = \" + 200px + \";\n" +
                "  for( var i = 0; i < document.images.length; i++ ) {\n" +
                "    var img = document.images[i];\n" +
                "    if( img.width > w ) {\n" +
                "      img.height = Math.round( img.height * ( w/img.width ) );\n" +
                "      img.width = w;\n" +
                "      img.style.display='block';\n" +
                "    };\n" +
                "  }\n" +
                "})();";
    }
}
