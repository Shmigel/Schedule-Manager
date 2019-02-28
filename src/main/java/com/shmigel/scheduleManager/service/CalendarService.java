package com.shmigel.scheduleManager.service;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.repository.CalendarRepository;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CalendarService {

    @Lazy
    @Autowired
    private CalendarService self;

    private CalendarRepository repository;

    private DateTimeUtil dateTimeUtil;

    @Autowired
    public CalendarService(CalendarRepository repository, DateTimeUtil dateTimeUtil) {
        log.info("Calendar service init");
        this.repository = repository;
        this.dateTimeUtil = dateTimeUtil;
    }

    private List<Event> upcomingEvents() {
        org.joda.time.DateTime day = dateTimeUtil.now().plusDays(5);
        return repository.upcomingEvents(10, dateTimeUtil.gNow(),
                dateTimeUtil.toGDateTime(day));
    }

    public List<Event> dayEvents(int dayOfMount) {
        Tuple2<DateTime, DateTime> dayPeriod = dateTimeUtil.dayPeriod(dayOfMount);
        return repository.upcomingEvents(10, dayPeriod._1, dayPeriod._2);
    }

    public Option<Event> eventByPosition(int dayOfMonth, int position) {
        return Option.of(Try.of(() -> dayEvents(dayOfMonth).get(position - 1)).get());
    }

    /**
     * Return live eventByPosition if present.
     * Since return from {@link CalendarRepository#upcomingEvents(int, DateTime, DateTime)}
     * is already ordered by startTime we get first and check if it's ongoing
     *
     * @return live eventByPosition if present
     */
    public Option<Event> liveEvent() {
        Event event = upcomingEvents().get(0);
        if (dateTimeUtil.isOngoing(event)) {
            return Option.of(event);
        }
        return Option.none();
    }

    public Event nextEvent() {
        return upcomingEvents().stream()
                .filter(dateTimeUtil::notOngoing).findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public void addUser(String email, String role) {
        repository.addUser(email, role);
    }

    public void allFuncTest() {
        log.debug("Load {} calendars", upcomingEvents().size());
        upcomingEvents().forEach((i) -> System.out.println(i.getId() + i.getSummary()));
        log.debug("Next eventByPosition {}",nextEvent().getSummary());
        log.debug("Live eventByPosition: {}", !liveEvent().isEmpty()? liveEvent().get().getSummary(): "false");
    }
}
