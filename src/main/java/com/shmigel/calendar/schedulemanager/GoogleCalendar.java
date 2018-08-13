package com.shmigel.calendar.schedulemanager;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@Scope("request")
public class GoogleCalendar {

//    @Bean
//    @Scope("request")
//    Calendar calendar(NetHttpTransport httpTransport,
//                      JacksonFactory jacksonFactory,
//                      GoogleCredential credential) {
//        return new Calendar(httpTransport, jacksonFactory, credential);
//    }

//    @Bean
//    @Scope("request")
//    GoogleCredential googleCredential() {
//        return new GoogleCredential().setFromTokenResponse(new TokenResponse().setAccessToken(authToken));
//    }


}
