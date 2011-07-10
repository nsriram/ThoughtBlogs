package com.tw.thoughtblogs;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.tw.thoughtblogs.model.Blog;
import com.tw.thoughtblogs.model.BlogData;
import com.tw.thoughtblogs.services.ThoughtBlogService;

import java.util.List;

import static com.tw.thoughtblogs.util.Constants.FEED_URL;

public class BlogListActivity extends ListActivity {
    private ProgressDialog progressDialog;

    private Context context() {
        return this.getApplicationContext();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        loadBlogs();
        startFeedContentService();
        registerForContextMenu(this.getListView());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setAdapter(dbFetch());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        TextView blogIdTextView = (TextView) v.findViewById(R.id.blog_id);
        String blogId = blogIdTextView.getText().toString();
        BlogData blogData = new BlogData(context());
        blogData.markRead(blogId);
        blogData.close();
        Intent showContent = new Intent(context(), BlogDetailActivity.class);
        showContent.setData(Uri.parse(blogId));
        startActivity(showContent);
    }

    private void setAdapter(List<Blog> blogs) {
        this.setListAdapter(new BlogAdapter(context(), R.layout.list_item, blogs));
    }

    private void loadBlogs() {
        initProgressDialog();
        new BlogDownloadTask().execute("");
    }

    private void setListContent(List<Blog> blogs) {
        dismissProgressDialog();
        setAdapter(blogs);
    }

    private void startFeedContentService() {
        Intent intent = new Intent(context(), ThoughtBlogService.class);
        startService(intent);
    }

    private List<Blog> dbFetch() {
        BlogData blogData = new BlogData(context());
        List<Blog> blogs = blogData.list();
        blogData.close();
        return blogs;
    }

    private void initProgressDialog() {
        progressDialog = ProgressDialog.show(BlogListActivity.this, "Downloading ... ", "Fetching Latest Entries", true, true);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private class BlogDownloadTask extends AsyncTask<String, Void, List<Blog>> {

        protected List<Blog> doInBackground(String... args) {

            BlogData blogData = new BlogData(context());
            String lastParsedDate = blogData.lastParsedDate();
            blogData.close();
            List<Blog> blogs = new RSSReader(FEED_URL).fetchLatestEntries(lastParsedDate);
            blogData = new BlogData(context());
            blogData.store(blogs);
            blogData.close();

            blogData = new BlogData(context());
            blogs = blogData.list();
            blogData.close();

            return blogs;
        }

        protected void onPostExecute(List<Blog> blogs) {
            BlogListActivity.this.setListContent(blogs);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == this.getListView().getId()) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Blog blog = (Blog) getListView().getItemAtPosition(info.position);
            menu.setHeaderTitle("Manage Blog Entry");
            menu.add(0, blog.getId(), 0, "Delete");
            menu.add(0, blog.getId(), 0, "Back");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete") {
            delete(item.getItemId() + "");
            setAdapter(dbFetch());
        } else {
            return false;
        }
        return true;
    }

    private void delete(String id) {
        BlogData blogData = new BlogData(context());
        blogData.delete(id);
        blogData.close();
    }
}
