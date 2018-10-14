package com.shmigel.scheduleManager.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.Tuple;
import com.shmigel.scheduleManager.service.DateTimeFormatters;
import io.vavr.Tuple2;
import org.springframework.stereotype.Component;

@Component
public class DateTimeUtil {

    public boolean isOngoing(Event event) {
        return event.getStart().getDateTime().getValue() <= gNow().getValue();
    }

    public boolean notOngoing(Event event) {
        return !isOngoing(event);
    }


    public boolean isToday(Event event) {
        return toJDateTime(event.getStart().getDateTime()).getDayOfYear() == toJDateTime(gNow()).getDayOfYear();
    }

    public boolean isTomorrow(Event event) {
        return toJDateTime(event.getStart().getDateTime()).getDayOfYear() == toJDateTime(gNow()).getDayOfYear()+1;
    }


    public Tuple2<DateTime, DateTime> dayPeriod(int dayOfMount) {
        return new Tuple2<>(
                toGDateTime(dayWithTime(dayOfMount, 0, 0)),
                toGDateTime(dayWithTime(dayOfMount, 23, 59)));
    }

    public Tuple2<DateTime, DateTime> todayPeriod() {
        return dayPeriod(now().getDayOfMonth());
    }

    public org.joda.time.DateTime dayWithTime(int dayOfMount, int hour, int minute) {
        return day(dayOfMount).withTime(hour, minute, 0, 0);
    }

    public org.joda.time.DateTime day(int dayOfMount) {
        return new org.joda.time.DateTime().withDayOfMonth(dayOfMount).withTime(0, 0, 0, 0);
    }


    public String startTime(Event event) {
        return toJDateTime(event.getStart().getDateTime()).toString(DateTimeFormatters.hourMinute.formatter());
    }

    public String endTime(Event event) {
        return toJDateTime(event.getEnd().getDateTime()).toString(DateTimeFormatters.hourMinute.formatter());
    }


    public DateTime gNow() {
        return new DateTime(System.currentTimeMillis());
    }

    public org.joda.time.DateTime now() {
        return new org.joda.time.DateTime(System.currentTimeMillis());
    }


    public org.joda.time.DateTime toJDateTime(DateTime googleDateTime) {
        return new org.joda.time.DateTime(googleDateTime.getValue());
    }

    public DateTime toGDateTime(org.joda.time.DateTime dateTime) {
        return new DateTime(dateTime.toString());
    }

}
