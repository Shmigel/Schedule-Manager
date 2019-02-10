package com.shmigel.scheduleManager.config;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shmigel.scheduleManager.service.CalendarService;
import com.shmigel.scheduleManager.service.Auth0TokenService;
import com.shmigel.scheduleManager.service.EventDescriptionParser;
import io.vavr.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

    @Autowired
    public GoogleBeanConfiguration(Auth0TokenService tokenManager) {
        this.tokenManager = tokenManager;
    }
    private Tuple2<String, String> tokens;
    private final Map<String, Tuple2<String, String>> tokenCache = new Hashtable<>();


    public void setAuth0Token(String userId, String userToken) {
        if (tokenCache.containsKey(userId)) {
            this.tokens = tokenCache.get(userId);
        } else {
            this.tokens = tokenManager.loadTokens(userToken);
            tokenCache.put(userId, tokens);
        }
    }

    @Bean
    @ConditionalOnExpression("'${tokens._1}'!=null")
    @Scope("prototype")
    GoogleCredential googleCredential() {
        return new GoogleCredential().setFromTokenResponse(new TokenResponse().setAccessToken(tokens._1));
    }

//    @Bean
//    @ConditionalOnExpression("'${tokens._2}'!=null")
//    @ConditionalOnMissingBean(GoogleCredential.class)
//    @Scope("prototype")
    GoogleCredential accessTokenGoogleCredential() {
        return new GoogleCredential().setFromTokenResponse(new TokenResponse().setRefreshToken(tokens._2));
    }

    @Bean
    @ConditionalOnExpression("'${tokens}'!=null")
    @Scope("prototype")
    CalendarService googleCalendar(JacksonFactory factory,
                                   NetHttpTransport httpTransport,
                                   GoogleCredential credential) {
        return new CalendarService(factory, httpTransport, credential);
    }

//    @Bean
//    @Scope("prototype")
//    GoogleTokenResponse tokenRequest(JacksonFactory factory,
//                                     NetHttpTransport httpTransport) throws IOException {
//        return new GoogleRefreshTokenRequest(httpTransport, factory , tokens, clientId, clientSecret).execute();
//    }



}
