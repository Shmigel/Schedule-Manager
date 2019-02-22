package com.shmigel.scheduleManager.util;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleBeansTuple {

    public final Calendar calendarManger;

    public final CalendarListEntry managedCalendar;

}