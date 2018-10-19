package com.shmigel.scheduleManager.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.shmigel.scheduleManager.GoogleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Optional;

public class CalendarService {

    private CalendarService calendarService;

    @Lazy
    @Autowired
    public void setCalendarService(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    private Calendar googleCalendar;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    private Logger logger = LoggerFactory.getLogger(CalendarService.class);

    private final static String defaultCalendarName = "ScheduleManagers' Calendar";
    public CalendarService(JacksonFactory jacksonFactory,
                           NetHttpTransport httpTransport, GoogleCredential googleCredential) {
        googleCalendar = new Calendar.Builder(httpTransport, jacksonFactory, googleCredential)
                .setApplicationName("scheduleManager").build();
    }

    private ThreadLocal<String> calendarIdCache = new ThreadLocal<>();

    /**
     * Return managed calendars' id with support of {@link ThreadLocal} caching, {@link #}.
     */
    private String calendarId() {
        return Option.of(calendarIdCache.get())
                .getOrElse(() -> {
                    calendarIdCache.set(managedCalendar().getId());
                    return calendarIdCache.get();
                });
    }

    /**
     * Load all and find new object of managed google calendar
     *
     * @throws GoogleException if couldn't either load calendars or find managed googleCalendar
     * @return object of managed googleCalendar
     */
    private CalendarListEntry managedCalendar() {
        List<CalendarListEntry> list = Try.of(() -> googleCalendar.calendarList().list().execute())
                .getOrElseThrow(GoogleException::new).getItems();

        return list.stream().filter(i -> i.getSummary().equals(defaultCalendarName))
                .findFirst().orElseThrow(GoogleException::new);
    }

    public List<Event> upcomingEvents(String calendarId, int maxResult,
                                      DateTime start, DateTime end) {
        logger.info("Loading new upcoming Events");
        return Try.of(() -> googleCalendar.events().list(calendarId)
                .setTimeMin(start)
                .setTimeMax(end)
                .setMaxResults(maxResult)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute()).getOrElseThrow(GoogleException::new)
                .getItems();
    }

    @Cacheable(value = "upcomingEvents")
    public List<Event> upcomingEvents(String calendarId) {
        org.joda.time.DateTime day = dateTimeUtil.now().plusDays(5);
        return calendarService.upcomingEvents(calendarId, 10, dateTimeUtil.gNow(),
                dateTimeUtil.toGDateTime(day));
    }

    public List<Event> dayEvents(int dayOfMount) {
        return dayEvents(dayOfMount, calendarId());
    }

    @Cacheable(value = "dayEvents")
    public List<Event> dayEvents(int dayOfMount, String calendarId) {
        Tuple2<DateTime, DateTime> dayPeriod = dateTimeUtil.dayPeriod(dayOfMount);
        return calendarService.upcomingEvents(calendarId(), 10, dayPeriod._1, dayPeriod._2);
    }

    public Option<Event> event(int dayOfMounth, int position) {
        return Option.of(Try.of(() -> dayEvents(dayOfMounth).get(position - 1)).get());
    }

    /**
     * Return live event if present.
     * Since return from {@link CalendarService#upcomingEvents(String userId)} is already ordered by startTime
     * we get first and check if it's ongoing
     *
     * @return live event if present
     */
    public Option<Event> liveEvent() {
        Event event = calendarService.upcomingEvents(calendarId()).get(0);
        if (dateTimeUtil.isOngoing(event)) {
            return Option.of(event);
        }
        return Option.none();
    }


    public Event nextEvent() {
        return calendarService.upcomingEvents(calendarId()).stream()
                .filter(dateTimeUtil::notOngoing).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void allFuncTest() {
        logger.debug("Load {} calendars", calendarService.upcomingEvents(calendarId()).size());
        calendarService.upcomingEvents(calendarId()).forEach((i) -> System.out.println(i.getId() + i.getSummary()));
        logger.debug("Next event {}",nextEvent().getSummary());
        logger.debug("Live event: {}", !liveEvent().isEmpty()? liveEvent().get().getSummary(): "false");
    }
}
