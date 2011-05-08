package com.tw.thoughtblogs.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.tw.thoughtblogs.Constants.*;

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
                + DATE + " TEXT NOT NULL );");
        database.execSQL("CREATE TABLE " + PARSE_CHECKPOINT_TABLE + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LAST_PARSED_DATE + " TEXT NOT NULL);");
        long insert = database.insert(PARSE_CHECKPOINT_TABLE, null, initTimeStamp(new GregorianCalendar(2011, 1, 1).getTime()));
        Log.v("BlogData", "ID=" + insert);
    }

    private ContentValues initTimeStamp(Date date) {
        ContentValues values = new ContentValues();
        String past = date.toString();
        Log.v("BlogData", "Last Parsed " + past);
        values.put(LAST_PARSED_DATE, past);
        return values;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        onCreate(db);
    }

    public void store(List<Blog> blogs) {
        SQLiteDatabase db = getWritableDatabase();
        Date now = new GregorianCalendar().getTime();
        for (Blog blog : blogs) {
            ContentValues values = new ContentValues();
            values.put(LINK, blog.getOrigLink());
            values.put(TITLE, blog.getTitle());
            values.put(DATE, blog.getPubDate());
            values.put(DESCRIPTION, blog.getDescription());
            db.insert(EVENTS_TABLE, null, values);
        }
        Log.v("BlogData ", "Blog Entries Stored " + blogs.size());
        ContentValues values = new ContentValues();
        values.put(LAST_PARSED_DATE, now.toString());
        db.update(PARSE_CHECKPOINT_TABLE, values, "_ID=1", null);
    }

    public Cursor load() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor entries = db.rawQuery("select title, _id from events order by _id desc", null);
        Log.v("BlogData ", "Loading " + entries.getCount());
        return entries;
    }

    public Date lastParsedDate() {
        SQLiteDatabase db = getReadableDatabase();
        Date date = null;
        Cursor notes = db.rawQuery("select " + LAST_PARSED_DATE + " from " + PARSE_CHECKPOINT_TABLE, null);
        if (notes.getCount() > 0 && notes.moveToNext()) {
            date = new Date(notes.getString(0));
        }
        notes.close();
        Log.v("BlogData", "LastParsedDate" + date);
        return date;
    }

    public Blog loadDescription(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Blog blog = null;
        Cursor descriptions = null;
        descriptions = db.rawQuery("select link,title,description from " + EVENTS_TABLE +
                " where _id=" + id, null);
        String details = null, title = null, link = null;
        if (descriptions.getCount() > 0 && descriptions.moveToNext()) {
            link = descriptions.getString(0);
            title = descriptions.getString(1);
            details = descriptions.getString(2);
            blog = new Blog(title, link, null, details);
        }
        descriptions.close();
        return blog;
    }
}
