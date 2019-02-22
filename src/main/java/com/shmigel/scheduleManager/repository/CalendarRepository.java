package com.shmigel.scheduleManager.repository;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.AclRule;
import com.google.api.services.calendar.model.Event;
import com.shmigel.scheduleManager.config.GoogleBeanConfiguration;
import com.shmigel.scheduleManager.util.GoogleBeansTuple;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CalendarRepository {

    @Autowired
    private GoogleBeanConfiguration googleBean;

    public List<Event> upcomingEvents(int maxResult,
                                      DateTime start, DateTime end) {
        GoogleBeansTuple calendar = googleBean.calendar();
        log.info("Loading new upcoming Events");
        return Try.of(() -> calendar.calendarManger.events().list(calendar.managedCalendar.getId())
                .setTimeMin(start).setTimeMax(end).setMaxResults(maxResult)
                .setOrderBy("startTime").setSingleEvents(true)
                .execute()).onFailure(RuntimeException::new).get()
                .getItems();
    }

    public void addUser(String email, String role) {
        GoogleBeansTuple calendar = googleBean.calendar();
        AclRule.Scope scope = new AclRule.Scope().setType("user").setValue(email);
        AclRule aclRule = new AclRule().setScope(scope).setRole(role);
        Try.of(() -> calendar.calendarManger.acl().insert(calendar.managedCalendar.getId(), aclRule).execute()).get();
    }

}
