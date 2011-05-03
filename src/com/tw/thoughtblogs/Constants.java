package com.tw.thoughtblogs;

public class Constants {
    public static final String FEED_URL = "http://feeds.feedburner.com/PlanetTw";
    //    public static final String FEED_URL = "http://news.google.com/news?pz=1&cf=all&ned=in&hl=en&topic=h&num=3&output=rss";
    public static final String UTF_8 = "UTF-8";
    public static final String ITEM = "item";
    public static final String ORIGLINK = "feedburner:origLink";
    public static final String PUBDATE = "pubDate";
    public static final int ONE_MINUTE = 60000;
    public static final String REFRESH_INTENT = "com.tw.thoughtblogs.REFRESH";

    //Database Constants
    public static final String _ID = "_id";
    public static final String DATABASE_NAME = "thoughtblogs.db";
    public static final String EVENTS_TABLE = "events";
    public static final String LINK = "link";
    public static final String TITLE = "title";

    public static final String DATE = "date";
    public static final String PARSE_CHECKPOINT_TABLE = "checkpoint";
    public static final String LAST_PARSED_DATE = "last_parsed_date";

    public static final int DATABASE_VERSION = 1;
    public static final String[] FROM = {TITLE, LINK, _ID};
    public static final int[] TO = {R.id.title, R.id.link};
    public static final String EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ = "EEE, dd MMM yyyy HH:mm:ss zzz";
}
