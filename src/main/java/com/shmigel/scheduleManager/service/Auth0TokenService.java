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

    private static Logger logger = LoggerFactory.getLogger(Auth0TokenService.class);

    private String getUserId(String auth0Token) throws UnirestException {
        HttpResponse<JsonNode> userInfoResponse = Unirest.get("https://schedule-manager.eu.auth0.com/userinfo")
                .header("Authorization", "Bearer " + auth0Token).asJson();
        return userInfoResponse.getBody().getObject().get("sub").toString();
    }

    private String getAuth0ManagerToken() throws UnirestException {
        String preparedBody = "{\n" +
                "\t\"grant_type\":\"client_credentials\",\n" +
                "\t\"client_id\": \"0rAJ4EzAJAKOhTbQ5R63EMbd6oRfxyO1\",\n" +
                "\t\"client_secret\": \"p_5XX3nmrXlBkZ9X084vQjkvy6ztg7xibg__TX1jRoZoG3upJGCKhfI29jF39zf8\",\n" +
                "\t\"audience\": \"https://schedule-manager.eu.auth0.com/api/v2/\"\n" +
                "}";
        HttpResponse<JsonNode> auth0ManagerTokenResponse = Unirest.post("https://schedule-manager.eu.auth0.com/oauth/token")
                .header("Content-Type", "application/json")
                .body(preparedBody)
                .asJson();
        return auth0ManagerTokenResponse.getBody().getObject().get("access_token").toString();
    }

    private String getAccessToken(String userId, String auth0ManagerToken) throws UnirestException {
        HttpResponse<JsonNode> accessTokenResonse = Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken).asJson();
        return accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0).get("access_token").toString();
    }

    public String load(String userToken) {
        String accessToken = null;
        try {
            String userId = getUserId(userToken);
            String auth0ManagerToken = getAuth0ManagerToken();
            accessToken = getAccessToken(userId, auth0ManagerToken);
            logger.info("userId:"+userId+" token:"+accessToken);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

}
