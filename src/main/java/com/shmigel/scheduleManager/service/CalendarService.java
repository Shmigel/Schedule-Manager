package com.shmigel.scheduleManager.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
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

    public void getCalendar() {
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = null;
        try {
            events = calendar.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }



    private String  toPrettyString(CalendarListEntry entry) {
        return entry.getId() +" "+ entry.getSummary() +"\n";
    }

}
