package com.shmigel.scheduleManager;

import com.google.api.client.json.GenericJson;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Lazy
public class GoogleCalendar {

    private static Logger logger = LoggerFactory.getLogger(GoogleCalendar.class);

    private static String defaultCalendarName = "ScheduleManagers' Calendar";

    @Lazy
    @Autowired
    Calendar calendar;

    private List<CalendarListEntry> getCalendars() {
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

    private void createCalendar(CalendarListEntry entry) {
        try {
            calendar.calendarList().insert(entry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertEvent(String calendarId, Event event) {
        try {
            calendar.events().insert(calendarId, event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String isCalendarPresent() {
        Optional<CalendarListEntry> entry = Optional.empty();
        for (CalendarListEntry i : getCalendars()) {
            if (i.getSummary().equals(defaultCalendarName)) {
                logger.debug("Found calendar with default name: {}", defaultCalendarName);
                entry = Optional.of(i);
            }
        }
        return entry.map(CalendarListEntry::getId).orElse("");
    }

    public void addEvent(Event event) {
        String calendarId = isCalendarPresent();
        insertEvent(calendarId, event);
    }

    private String  toPrettyString(CalendarListEntry entry) {
        return entry.getId() +" "+ entry.getSummary() +"\n";
    }

    public String createSchedule() {
        return isCalendarPresent();
//        if                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     (!isCalendarPresent()) {
//            createCalendar(new CalendarListEntry().setSummary(defaultCalendarName));
//        }
    }

//    public Integer getCalendar

//    private Calendar calendar() {
//    }

    //    public Calendar.CalendarList.List getCalendarList() throws IOException {
//        return calendar.calendarList().list();
//    }

}
