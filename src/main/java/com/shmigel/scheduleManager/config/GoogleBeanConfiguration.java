package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Configuration
public class GoogleBeanConfiguration {


    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    private final Auth0TokenService tokenManager;

    private static Logger logger = LoggerFactory.getLogger(GoogleBeanConfiguration.class);

    @Autowired
    public GoogleBeanConfiguration(Auth0TokenService tokenManager) {
        this.tokenManager = tokenManager;
    }
    private String refreshToken = "asd";
    private final Map<String, String> tokenCache = new Hashtable<>();


    public void setAuth0Token(String userId, String userToken) {
        if (tokenCache.containsKey(userId)) {
            logger.info("from cache: {}", userId);
            this.refreshToken = tokenCache.get(userId);
        } else {
            this.refreshToken = tokenManager.loadRefreshToken(userToken);
            tokenCache.put(userId, refreshToken);
            logger.info("from auth0: {}", userId);
        }
    }

    @Bean
    @Scope("prototype")
    GoogleCredential googleCredential(TokenResponse tokenResponse) {
        return new GoogleCredential().setFromTokenResponse(tokenResponse);
    }

    @Bean
    @ConditionalOnExpression("'${refreshToken}'!=null")
    @Scope("prototype")
    CalendarService googleCalendar(JacksonFactory factory,
                                   NetHttpTransport httpTransport,
                                   GoogleCredential credential) {
        return new CalendarService(factory, httpTransport, credential);
    }

    @Bean
    @ConditionalOnExpression("'${refreshToken}'!=null")
    @Scope("prototype")
    GoogleTokenResponse tokenRequest(JacksonFactory factory,
                                     NetHttpTransport httpTransport) throws IOException {
        return new GoogleRefreshTokenRequest(httpTransport, factory , refreshToken, clientId, clientSecret).execute();
    }

}
