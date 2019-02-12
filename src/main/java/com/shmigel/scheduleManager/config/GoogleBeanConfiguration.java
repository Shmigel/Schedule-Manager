package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import com.shmigel.scheduleManager.service.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class GoogleBeanConfiguration {

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    private final Auth0TokenService tokenManager;

    @Autowired
    public GoogleBeanConfiguration(Auth0TokenService tokenManager) {
        this.tokenManager = tokenManager;
    }

    private String auth0Token;

    public void setAuth0Token(String auth0Token) {
        this.auth0Token = auth0Token;
    }

    @Bean
    @Scope("prototype")
    GoogleCredential googleCredential(JacksonFactory factory,
                                      NetHttpTransport httpTransport) {
        //TODO: In case refresh token is missing
        String refreshToken = tokenManager.getTokens(auth0Token)._2;
        log.debug("Refresh token {}", refreshToken);
        return new GoogleCredential.Builder().setTransport(httpTransport)
                .setJsonFactory(factory)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(new TokenResponse().setRefreshToken(refreshToken));
    }

    @Bean
    @ConditionalOnExpression("'${tokens}'!=null")
    @Scope("prototype")
    CalendarService googleCalendar(JacksonFactory factory,
                                   NetHttpTransport httpTransport,
                                   GoogleCredential credential) {
        return new CalendarService(factory, httpTransport, credential);
    }
}
