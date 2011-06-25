package com.tw.thoughtblogs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.tw.thoughtblogs.util.Constants.*;

public class BlogData extends SQLiteOpenHelper {

    public BlogData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE " + EVENTS_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LINK + " TEXT NOT NULL,"
                + TITLE + " TEXT NOT NULL,"
                + DESCRIPTION + " TEXT NOT NULL,"
                + STATUS + " INTEGER NOT NULL,"
                + DATE + " TEXT NOT NULL );");
        database.execSQL("CREATE TABLE " + PARSE_CHECKPOINT_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAST_PARSED_DATE + " TEXT NOT NULL);");
        database.insert(PARSE_CHECKPOINT_TABLE, null, initTimeStamp());
    }

    private ContentValues initTimeStamp() {
        ContentValues values = new ContentValues();
        values.put(LAST_PARSED_DATE, INITIAL_TIME_STAMP);
        return values;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + PARSE_CHECKPOINT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        onCreate(db);
    }

    public void store(List<Blog> blogs) {
        if (blogs == null || blogs.isEmpty())
            return;
        SQLiteDatabase db = getWritableDatabase();
        for (Blog blog : blogs) {
            ContentValues values = new ContentValues();
            values.put(LINK, blog.getOrigLink());
            values.put(TITLE, blog.getTitle());
            values.put(DATE, blog.getPubDate());
            values.put(DESCRIPTION, blog.getDescription());
            values.put(STATUS, blog.getStatus());
            db.insert(EVENTS_TABLE, null, values);
        }
        Log.v("BlogData ", "Blog Entries Stored " + blogs.size());
        String lastParsedDate = blogs.get(blogs.size() - 1).getPubDate();
        ContentValues values = new ContentValues();
        values.put(LAST_PARSED_DATE, lastParsedDate);
        Log.v("lastParsedDate ", "lastParsedDate=" + lastParsedDate);
        db.update(PARSE_CHECKPOINT_TABLE, values, "_ID=1", null);
    }

    public List<Blog> list() {
        SQLiteDatabase db = getReadableDatabase();
        List<Blog> blogs = new ArrayList<Blog>();
        Cursor entries = db.rawQuery("select _id,title,status from events order by _id desc", null);
        while (entries.getCount() > 0 && entries.moveToNext()) {
            Blog blog = new Blog(entries.getInt(0), entries.getString(1), null, null, null, entries.getInt(2));
            blogs.add(blog);
        }
        entries.close();
        return blogs;
    }

    public String lastParsedDate() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor notes = db.rawQuery("select last_parsed_date from checkpoint", null);
        String lastParsedDate = null;
        if (notes.getCount() > 0 && notes.moveToNext()) {
            lastParsedDate = notes.getString(0);
            Log.v("BlogData", "" + lastParsedDate);
        }
        notes.close();
        return lastParsedDate;
    }

    public Blog loadDescription(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Blog blog = null;
        Cursor cursor = db.rawQuery("select link,title,description,status from events where _id=" + id, null);
        if (cursor.getCount() > 0 && cursor.moveToNext()) {
            blog = new Blog(cursor.getString(1), cursor.getString(0), null, cursor.getString(2), cursor.getInt(3));
        }
        if (!cursor.isClosed())
            cursor.close();
        return blog;
    }

    public void markRead(String id) {
        getReadableDatabase().execSQL("update events set status = 0 where _id=" + id);
    }
}