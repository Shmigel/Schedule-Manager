package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

@Configuration
public class GoogleBeanConfiguration {

    private String accessToken;

    @Autowired
    private Auth0TokenService tokenManager;

    public void setAuth0Token(String auth0Token) {
        String accessToken = tokenManager.load(auth0Token);
        this.accessToken = accessToken;
    }

    @Bean
    @Lazy
    @Scope("prototype")
    GoogleCredential googleCredential() {
        accessToken.toString();
        return new GoogleCredential().setFromTokenResponse(new TokenResponse().setAccessToken(accessToken));
    }

    @Bean
    @Lazy
    @Scope("prototype")
    CalendarService googleCalendar(JacksonFactory factory,
                                   NetHttpTransport httpTransport,
                                   GoogleCredential credential) {
        return new CalendarService(factory, httpTransport, credential);
    }

}
