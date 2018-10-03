package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shmigel.scheduleManager.Tuple;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Configuration
public class GoogleBeanConfiguration {

    /**
     * Store access and refresh token.
     * Set at base(/) post handler {@link com.shmigel.scheduleManager.controller.BaseController()}
     */
    private Tuple<String, String> tokens;

    @Value("${google.clientId}")
    private String clientId;

    @Value("${google.clientSecret}")
    private String clientSecret;

    @Autowired
    private Auth0TokenService tokenManager;

    public void setAuth0Token(String auth0Token) {
        this.tokens = tokenManager.load(auth0Token);
    }

    private static Logger logger = LoggerFactory.getLogger(GoogleBeanConfiguration.class);

    @Bean
    @Lazy
    @Scope("prototype")
    GoogleCredential googleCredential(TokenRequest tokenRequest) throws IOException {
        return new GoogleCredential().setFromTokenResponse(tokenRequest.execute());
    }

    @Bean
    @Lazy
    @Scope("prototype")
    CalendarService googleCalendar(JacksonFactory factory,
                                   NetHttpTransport httpTransport,
                                   GoogleCredential credential) {
        return new CalendarService(factory, httpTransport, credential);
    }

    @Bean
    @Lazy
    @Scope("prototype")
    GoogleRefreshTokenRequest tokenRequest(JacksonFactory factory,
                                           NetHttpTransport httpTransport) {
        return new GoogleRefreshTokenRequest(httpTransport, factory , tokens.getSecond(), clientId, clientSecret);
    }

}
