package com.shmigel.scheduleManager;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.shmigel.scheduleManager.config.GoogleBeanConfiguration;
import com.shmigel.scheduleManager.exception.GoogleCalendarException;
import com.shmigel.scheduleManager.repository.CalendarRepository;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.util.DateTimeUtil;
import com.shmigel.scheduleManager.util.GoogleBeansTuple;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class CalendarRepositoryTest {

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    @Value("${project.test.refreshToken}")
    private String refreshToken;

    @Value("${project.defaultCalendarName}")
    private String defaultCalendarName;

    @Autowired
    private JacksonFactory factory;

    @Autowired
    private NetHttpTransport httpTransport;

    @Autowired
    private DateTimeUtil dateTimeUtil;

    private CalendarService calendarService;

    private CalendarRepository calendarRepository;

    @Before
    void setUp() {
        GoogleBeanConfiguration mock = mock(GoogleBeanConfiguration.class);
        when(mock.calendar()).thenReturn(load());
        calendarRepository = new CalendarRepository(mock);
        calendarService = new CalendarService(calendarRepository, dateTimeUtil);
    }

    private GoogleBeansTuple load() {
        GoogleCredential credential = new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(factory)
                .setClientSecrets(clientId, clientSecret)
                .build().setFromTokenResponse(new TokenResponse().setRefreshToken(refreshToken));

        Calendar calendar = new Calendar.Builder(httpTransport, factory, credential)
                .build();

        List<CalendarListEntry> list = Try.of(() -> calendar.calendarList().list().execute())
                .getOrElseThrow(() -> new GoogleCalendarException("Could'nt load calendar list")).getItems();

        CalendarListEntry calendarListEntry = list.stream().filter(i -> i.getSummary().equals(defaultCalendarName))
                .findFirst().orElseThrow(() -> {
                    log.error("Calendar with default managed calendar name wasn't found");
                    return new GoogleCalendarException("Calendar with default managed calendar name wasn't found");
                });

        return new GoogleBeansTuple(calendar, calendarListEntry);
    }

}
