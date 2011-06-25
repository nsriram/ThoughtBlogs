package com.tw.thoughtblogs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    private DateFormat format;

    public DateFormatter(String formatString) {
        this.format = new SimpleDateFormat(formatString);
    }

    public boolean isParsed(String toParseDate, String lastParsed) throws ParseException {
        Date date = format.parse(toParseDate);
        Date lastParsedDate = format.parse(lastParsed);
        return lastParsedDate.equals(date) || lastParsedDate.after(date);
    }
}
