package com.shmigel.scheduleManager;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class BeansConfiguration {

    private String authToken = "";

    private static Logger logger = LoggerFactory.getLogger(BeansConfiguration.class);

    @Autowired
    private RestTemplate restTemplate;

    public void setAuthToken(String authToken) {
        logger.debug("New authToken: {}",authToken);
        this.authToken = authToken;
    }

    @Bean
    @Lazy
    @Scope("prototype")
    Calendar calendar(JacksonFactory jacksonFactory,
                      NetHttpTransport netHttpTransport,
                      GoogleCredential googleCredential) {
        return new Calendar(netHttpTransport, jacksonFactory, googleCredential);
    }

    @Bean
    @Lazy
    @Scope("prototype")
    GoogleCredential googleCredential() {
        return new GoogleCredential().setFromTokenResponse(new TokenResponse().setAccessToken(authToken));
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    NetHttpTransport netHttpTransport() {
        NetHttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return httpTransport;
    }

    @Bean
    JacksonFactory jacksonFactory() {
        return JacksonFactory.getDefaultInstance();
    }
}
