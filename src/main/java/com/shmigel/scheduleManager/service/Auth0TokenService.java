package com.shmigel.scheduleManager.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Since request from dialogflow gives auth0 access token instead of google access token,
 * we need to perform few additional request to get relevant access token
 * witch we will use to access to google calendar api
 */

@Service
public class Auth0TokenService {

    /**
     * Access token for auth0 account manager
     */
    private final String auth0ManagerToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImtpZCI6Ik1ERTVOa0pDUlRJMU1qQkJSak5FTUVGR1JqQTRORU16TnpWR016a3hNemcxTVRFM01UWTJNQSJ9.eyJpc3MiOiJodHRwczovL3NjaGVkdWxlLW1hbmFnZXIuZXUuYXV0aDAuY29tLyIsInN1YiI6IjByQUo0RXpBSkFLT2hUYlE1UjYzRU1iZDZvUmZ4eU8xQGNsaWVudHMiLCJhdWQiOiJodHRwczovL3NjaGVkdWxlLW1hbmFnZXIuZXUuYXV0aDAuY29tL2FwaS92Mi8iLCJpYXQiOjE1Mzc3MDM5NDYsImV4cCI6MTUzNzc5MDM0NiwiYXpwIjoiMHJBSjRFekFKQUtPaFRiUTVSNjNFTWJkNm9SZnh5TzEiLCJzY29wZSI6InJlYWQ6dXNlcnMgcmVhZDp1c2VyX2lkcF90b2tlbnMiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMifQ.QwkTAwAxjM-RqOJNR9i-kpCLIYKXOes12yBUIQEQEL0JZThlYUwj1AidZCCP-us5s3vjAw97G4dv-ACKYnN20AHQYYRX1MCzuvvSaC5Wd0JOzdh-XVaUDlP1lSKXI3KQR2v_bSvXo8Xbdi2YthNZMIIFsjf8tadcDlc51bzvmCO1o0nqRJsrhbeetOgPmbVSNF5r-BRgLYy0T8G_W4SI3nxU2CAmT23JI3hgDNFa61C0DFxCW2L8TiUhdiOouAvRa5GVi3g5WGqaZClhfVnNtqF0jl7ngTmKCY9qv1FmJvXaplj8jAsPOuCGfqEibWNCCSAqQjw-o9FqLKPbR11Cmw";

    private static Logger logger = LoggerFactory.getLogger(Auth0TokenService.class);

    private String getUserId(String auth0Token) throws UnirestException {
        HttpResponse<JsonNode> userInfoResponse = Unirest.get("https://schedule-manager.eu.auth0.com/userinfo")
                .header("Authorization", "Bearer " + auth0Token).asJson();
        return userInfoResponse.getBody().getObject().get("sub").toString();
    }

    private String getAccessToken(String userId) throws UnirestException {
        HttpResponse<JsonNode> accessTokenResonse = Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken).asJson();
        return accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0).get("access_token").toString();
    }

    public String load(String userToken) {
        String accessToken = null;
        try {
            String userId = getUserId(userToken);
            accessToken = getAccessToken(userId);
            logger.info("userId:"+userId+" token:"+accessToken);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}
