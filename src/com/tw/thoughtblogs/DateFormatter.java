package com.tw.thoughtblogs;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
    private DateFormat format;

    public DateFormatter(String formatString) {
        this.format = new SimpleDateFormat(formatString);
    }

    public boolean isParsed(String toParseDate, Date lastParsedDate) throws ParseException {
        Date date = format.parse(toParseDate);
        return lastParsedDate.after(date);
    }
}
