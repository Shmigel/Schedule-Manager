package com.shmigel.scheduleManager.util;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.service.DateTimeFormatters;
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
        return toJodaDateTime(event.getStart().getDateTime()).getDayOfYear() == toJodaDateTime(gNow()).getDayOfYear();
    }

    public boolean isTomorrow(Event event) {
        return toJodaDateTime(event.getStart().getDateTime()).getDayOfYear() == toJodaDateTime(gNow()).getDayOfYear()+1;
    }

    public DateTime gNow() {
        return new DateTime(System.currentTimeMillis());
    }

    public String startTime(Event event) {
        return toJodaDateTime(event.getStart().getDateTime()).toString(DateTimeFormatters.hourMinute.formatter());
    }

    public String endTime(Event event) {
        return toJodaDateTime(event.getEnd().getDateTime()).toString(DateTimeFormatters.hourMinute.formatter());
    }

    public org.joda.time.DateTime now() {
        return new org.joda.time.DateTime(System.currentTimeMillis());
    }

    public org.joda.time.DateTime toJodaDateTime(DateTime googleDateTime) {
        return new org.joda.time.DateTime(googleDateTime.getValue());
    }

}
