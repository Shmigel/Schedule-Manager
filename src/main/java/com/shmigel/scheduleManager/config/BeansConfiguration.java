package com.shmigel.scheduleManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Configuration
public class BeansConfiguration {

    private static Logger logger = LoggerFactory.getLogger(BeansConfiguration.class);

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    JacksonFactory jacksonFactory() {
        return JacksonFactory.getDefaultInstance();
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
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
