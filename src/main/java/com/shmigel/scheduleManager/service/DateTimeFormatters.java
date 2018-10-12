package com.shmigel.scheduleManager.service;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum DateTimeFormatters {

    dayOfWeak(DateTimeFormat.forPattern("EEEE")),
    monthDay(DateTimeFormat.forPattern("MMMM dd")),
    hourMinute(DateTimeFormat.forPattern("HH:mm"));

    private DateTimeFormatter formatter;

    DateTimeFormatters(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    public DateTimeFormatter formatter() {
        return formatter;
    }
}
