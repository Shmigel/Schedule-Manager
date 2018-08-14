package com.shmigel.scheduleManager;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
