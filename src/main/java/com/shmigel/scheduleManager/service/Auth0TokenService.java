package com.shmigel.scheduleManager.service;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.BaseRequest;
import com.shmigel.scheduleManager.TokenMissingException;
import com.shmigel.scheduleManager.Tuple;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    private String getUserId(String auth0Token) {
        HttpResponse<JsonNode> userInfoResponse = jsonNodeResponse(Unirest.get("https://schedule-manager.eu.auth0.com/userinfo")
                .header("Authorization", "Bearer " + auth0Token));
        return Optional.ofNullable(userInfoResponse.getBody().getObject().get("sub").toString()).orElseThrow(TokenMissingException::new);
    }

    private String getAuth0ManagerToken() {
        HttpResponse<JsonNode> auth0ManagerTokenResponse = jsonNodeResponse(Unirest.post("https://schedule-manager.eu.auth0.com/oauth/token")
                .header("Content-Type", "application/json")
                .body(preparedBody()));
        return Optional.ofNullable(auth0ManagerTokenResponse.getBody().getObject().get("access_token").toString()).orElseThrow(TokenMissingException::new);
    }

    private Tuple<String, String> getGoogleTokens(String userId, String auth0ManagerToken) {
        HttpResponse<JsonNode> accessTokenResonse = jsonNodeResponse(Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken));

        JSONObject identities = accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0);
        return new Tuple<>(identities.get("access_token").toString(), identities.get("refresh_token").toString());
    }

    private HttpResponse<JsonNode> jsonNodeResponse(BaseRequest request) {
        HttpResponse<JsonNode> response = null;
        try {
            response = request.asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Tuple<String, String> load(String userToken) {
        String userId = getUserId(userToken);
        String auth0ManagerToken = getAuth0ManagerToken();

        Tuple<String, String> tokens = getGoogleTokens(userId, auth0ManagerToken);
        logger.info("userId:"+userId+" access_token:"+tokens.getFirst()+", refresh_token:"+tokens.getSecond());
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
