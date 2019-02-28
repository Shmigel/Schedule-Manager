package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.shmigel.scheduleManager.controller.BaseController;
import com.shmigel.scheduleManager.exception.GoogleCalendarException;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import com.shmigel.scheduleManager.util.GoogleBeansTuple;
import io.vavr.Tuple2;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class GoogleBeanConfiguration {

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    @Value("${project.name}")
    private String projectName;

    @Value("${project.defaultCalendarName}")
    private String defaultCalendarName;

    @Autowired
    private Auth0TokenService tokenManager;

    @Autowired
    private JacksonFactory factory;

    @Autowired
    private NetHttpTransport httpTransport;

    @Autowired
    private GoogleBeanConfiguration self;

    private String auth0Token;

    /**
     * Used by {@link BaseController} to set auth0Token from request
     * @param auth0Token auth0 token
     */
    public void setAuth0Token(String auth0Token) {
        this.auth0Token = auth0Token;
    }

    @Cacheable
    private GoogleCredential googleCredential(String auth0Token) {
        log.info("Building google credential on auth0Token: {}", auth0Token);
        Tuple2<String, String> googleToken = tokenManager.getTokens(auth0Token);

        if (!googleToken._2.isEmpty()) {
            log.debug("Base on refresh token: {}", googleToken._2);
            return new GoogleCredential.Builder().setTransport(httpTransport)
                    .setJsonFactory(factory)
                    .setClientSecrets(clientId, clientSecret)
                    .build().setFromTokenResponse(new TokenResponse().setRefreshToken(googleToken._2));
        }else if(!googleToken._1.isEmpty()) {
            log.debug("Base on access token: {}", googleToken._1);
            return new GoogleCredential.Builder().setTransport(httpTransport)
                    .setJsonFactory(factory)
                    .setClientSecrets(clientId, clientSecret)
                    .build().setFromTokenResponse(new TokenResponse().setAccessToken(googleToken._1));
        }else {
            log.error("Both google token is missing");
            throw new RuntimeException("Both google token is missing");
        }
    }

    public GoogleBeansTuple calendar() {
        Calendar calendarManager = self.calendarManager(auth0Token);
        CalendarListEntry managedCalendar = self.managedCalendar(calendarManager);
        return new GoogleBeansTuple(calendarManager, managedCalendar);
    }

    @Cacheable
    private Calendar calendarManager(String auth0Token) {
        log.info("Creating GoogleCredential on auth0Token: {}", auth0Token);
        GoogleCredential credential = googleCredential(auth0Token);
        return new Calendar.Builder(httpTransport, factory, credential)
                .setApplicationName(projectName).build();
    }

    @Cacheable
    private CalendarListEntry managedCalendar(Calendar googleCalendar) {
        log.info("Looking for managed calendar from: {}", googleCalendar);
        List<CalendarListEntry> list = Try.of(() -> googleCalendar.calendarList().list().execute())
                .getOrElseThrow(() -> new GoogleCalendarException("Could'nt load calendar list")).getItems();

        return list.stream().filter(i -> i.getSummary().equals(defaultCalendarName))
                .findFirst().orElseThrow(() -> {
                    log.error("Calendar with default managed calendar name wasn't found");
                    return new GoogleCalendarException("Calendar with default managed calendar name wasn't found");
                });
    }
}
