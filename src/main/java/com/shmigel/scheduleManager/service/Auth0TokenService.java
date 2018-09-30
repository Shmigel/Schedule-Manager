package com.shmigel.scheduleManager.service;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.shmigel.scheduleManager.Tuple;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Since request from dialogflow gives auth0 access token instead of google access token,
 * we need to perform few additional request to get relevant access token
 * witch we will use to access to google calendar api
 */

@Service
public class Auth0TokenService {

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    @Autowired
    @Lazy
    private GoogleRefreshTokenRequest tokenRequest;

    private static Logger logger = LoggerFactory.getLogger(Auth0TokenService.class);

    private String getUserId(String auth0Token) throws UnirestException {
        HttpResponse<JsonNode> userInfoResponse = Unirest.get("https://schedule-manager.eu.auth0.com/userinfo")
                .header("Authorization", "Bearer " + auth0Token).asJson();
        return userInfoResponse.getBody().getObject().get("sub").toString();
    }

    private String getAuth0ManagerToken() throws UnirestException {
        JSONObject object = new JSONObject();
        HttpResponse<JsonNode> auth0ManagerTokenResponse = Unirest.post("https://schedule-manager.eu.auth0.com/oauth/token")
                .header("Content-Type", "application/json")
                .body(preparedBody())
                .asJson();
        return auth0ManagerTokenResponse.getBody().getObject().get("access_token").toString();
    }

    private String getAccessToken(String userId, String auth0ManagerToken) throws UnirestException {
        HttpResponse<JsonNode> accessTokenResonse = Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken).asJson();
        return accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0).get("access_token").toString();
    }

    private Tuple<String, String> getGoogleTokens(String userId, String auth0ManagerToken) throws UnirestException {
        HttpResponse<JsonNode> accessTokenResonse = Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken).asJson();
        JSONObject identities = accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0);
        return new Tuple<>(identities.get("access_token").toString(), identities.get("refresh_token").toString());
    }

    public Tuple<String, String> load(String userToken) {
        Tuple<String, String> tokens = null;
        try {
            String userId = getUserId(userToken);
            String auth0ManagerToken = getAuth0ManagerToken();
            tokens = getGoogleTokens(userId, auth0ManagerToken);
            logger.info("userId:"+userId+" access_token:"+tokens.getFirst()+", refresh_token:"+tokens.getSecond());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    private JSONObject preparedBody() {
        return new JSONObject()
                .put("grant_type", "client_credentials")
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("audience", audience);
    }

}
