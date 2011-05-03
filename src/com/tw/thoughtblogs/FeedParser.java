package com.tw.thoughtblogs;

import android.util.Log;
import com.tw.thoughtblogs.model.Blog;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tw.thoughtblogs.Constants.*;
import static org.xmlpull.v1.XmlPullParser.*;

public class FeedParser {

    public List<Blog> parse(InputStream content, Date lastParsedDate) throws Exception {
        List<Blog> entries = new ArrayList<Blog>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(content, UTF_8);
        int eventType = xpp.getEventType();
        boolean stopParsing = false;
        DateFormatter formatter = new DateFormatter(EEE_DD_MMM_YYYY_HH_MM_SS_ZZZ);
        while (eventType != END_DOCUMENT && !stopParsing) {
            if (eventType == START_TAG && xpp.getName() != null && xpp.getName().equals(ITEM)) {
                Blog blog = parseItem(xpp, eventType);
                if (formatter.isParsed(blog.getPubDate(), lastParsedDate)) {
                    stopParsing = true;
                } else {
                    Log.v("FeedParser ", blog.getTitle());
                    entries.add(blog);
                }
            }
            eventType = xpp.next();
        }
        return entries;
    }

    private Blog parseItem(XmlPullParser xpp, final int parentEventType) throws XmlPullParserException, IOException {
        int eventType = parentEventType;
        String title = "", origLink = "", pubDate = "";
        boolean continueParsingItem = true;
        while (continueParsingItem) {
            if (eventType == START_TAG && xpp.getName() != null) {
                String tagName = xpp.getName();
                if (tagName.equals(TITLE)) {
                    title = xpp.nextText();
                }
                if (xpp.getName().equals(ORIGLINK)) {
                    origLink = xpp.nextText();
                }
                if (xpp.getName().equals(PUBDATE)) {
                    pubDate = xpp.nextText();
                }
            }
            if (eventType == END_TAG && xpp.getName() != null && xpp.getName().equals(ITEM)) {
                continueParsingItem = false;
            }
            eventType = xpp.next();
        }
        return new Blog(title, origLink, pubDate);
    }
}
