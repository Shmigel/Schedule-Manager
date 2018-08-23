package com.shmigel.scheduleManager;

import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Lazy
//@Scope("prototype")
public class GoogleCalendar {

    @Lazy
    @Autowired
    Calendar calendar;

    public Calendar.CalendarList.List getCalendar() throws IOException {
        return calendar.calendarList().list();
    }

    //    public Calendar.CalendarList.List getCalendarList() throws IOException {
//        return calendar.calendarList().list();
//    }

}
