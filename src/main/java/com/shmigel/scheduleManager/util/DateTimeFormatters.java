package com.shmigel.scheduleManager.util;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum DateTimeFormatters {

    dayOfWeak(DateTimeFormat.forPattern("EEEE")),   // Monday
    monthDay(DateTimeFormat.forPattern("MMMM dd")), // October 15
    hourMinute(DateTimeFormat.forPattern("HH:mm")); // 12:00

    private DateTimeFormatter formatter;

    DateTimeFormatters(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }
}
