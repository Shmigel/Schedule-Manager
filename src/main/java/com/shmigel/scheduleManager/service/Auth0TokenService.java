package com.shmigel.scheduleManager.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import com.shmigel.scheduleManager.GoogleException;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Since request from dialogflow gives auth0 access token instead of google access/refresh tokens,
 * we need to perform few additional request to get relevant access/refresh tokens
 * witch will be used to access to google calendar api
 */

@Service
public class Auth0TokenService {

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    private static Logger logger = LoggerFactory.getLogger(Auth0TokenService.class);

    private String getUserId(String auth0Token) {
        HttpResponse<JsonNode> userInfoResponse = jsonNodeResponse(Unirest.get("https://schedule-manager.eu.auth0.com/userinfo")
                .header("Authorization", "Bearer " + auth0Token));

        return Option.of(userInfoResponse.getBody().getObject().get("sub").toString())
                .getOrElseThrow(GoogleException::new);
    }

    private String getAuth0ManagerToken() {
        HttpResponse<JsonNode> auth0ManagerTokenResponse = jsonNodeResponse(Unirest.post("https://schedule-manager.eu.auth0.com/oauth/token")
                .header("Content-Type", "application/json")
                .body(preparedBody()));

        return Option.of(auth0ManagerTokenResponse.getBody().getObject().get("access_token").toString())
                .getOrElseThrow(GoogleException::new);
    }

    private Tuple2<String, String> getGoogleTokens(String userId, String auth0ManagerToken) {
        HttpResponse<JsonNode> accessTokenResonse = jsonNodeResponse(Unirest.get("https://schedule-manager.eu.auth0.com/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken));

        JSONObject identities = accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0);
        return new Tuple2<>(identities.get("access_token").toString(), identities.get("refresh_token").toString());
    }

    private HttpResponse<JsonNode> jsonNodeResponse(BaseRequest request) {
        return Try.of(request::asJson).getOrElseThrow(() -> new RuntimeException());
    }

    public Tuple2<String, String> loadTokens(String userToken) {
        String userId = getUserId(userToken);
        String auth0ManagerToken = getAuth0ManagerToken();

        Tuple2<String, String> tokens = getGoogleTokens(userId, auth0ManagerToken);
        logger.info("userId:"+userId+" access_token:"+ tokens._1 +", refresh_token:"+ tokens._2);
        return tokens;
    }

    public String loadRefreshToken(String userToken) {
        Tuple2<String, String> load = loadTokens(userToken);
        return load._2();
    }

    private JSONObject preparedBody() {
        return new JSONObject()
                .put("grant_type", "client_credentials")
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("audience", audience);
    }

}
