package com.shmigel.scheduleManager.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CalendarService {

    private Calendar calendar;

    private Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private static String defaultCalendarName = "ScheduleManagers' Calendar";

    public CalendarService(JacksonFactory jacksonFactory,
                           NetHttpTransport httpTransport, GoogleCredential googleCredential) {
        calendar = new Calendar(httpTransport, jacksonFactory, googleCredential);
    }

    public List<CalendarListEntry> getCalendars() {
        List<CalendarListEntry> items = null;
        try {
            items = calendar.calendarList().list().execute().getItems();
            logger.debug("Loaded {} calendars: {}", items.size(), items.stream()
                    .map(this::toPrettyString).collect(toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private String  toPrettyString(CalendarListEntry entry) {
        return entry.getId() +" "+ entry.getSummary() +"\n";
    }

}
