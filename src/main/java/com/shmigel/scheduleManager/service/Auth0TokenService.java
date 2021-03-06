package com.shmigel.scheduleManager.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.BaseRequest;
import com.shmigel.scheduleManager.exception.Auth0Exception;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Since request from dialogflow gives auth0 access token instead of google access/refresh tokens,
 * i need to perform few additional request to get relevant access/refresh tokens
 * witch will be used to access to google calendarManager api
 */
@Slf4j
@Service
public class Auth0TokenService {

    @Value("${auth0.clientId}")
    private String clientId;

    @Value("${auth0.clientSecret}")
    private String clientSecret;

    @Value("${auth0.audience}")
    private String audience;

    @Value("${auth0.baseUrl}")
    private String baseUrl;

    private String getUserId(String auth0Token) {
        HttpResponse<JsonNode> userInfoResponse = jsonNodeResponse(Unirest.get(baseUrl +"/userinfo")
                .header("Authorization", "Bearer " + auth0Token));

        return Option.of(userInfoResponse.getBody().getObject().get("sub").toString())
                .getOrElseThrow(() -> new Auth0Exception("sub(id) is missing in auth0 /userinfo response"));
    }

    private String getAuth0ManagerToken() {
        HttpResponse<JsonNode> auth0ManagerTokenResponse = jsonNodeResponse(Unirest.post(baseUrl +"/oauth/token")
                .header("Content-Type", "application/json")
                .body(preparedBody()));

        return Option.of(auth0ManagerTokenResponse.getBody().getObject().get("access_token").toString())
                .getOrElseThrow(() -> new Auth0Exception("access_token si missing in aoth0 /oauth/token response"));
    }

    private Tuple2<String, String> getGoogleTokens(String userId, String auth0ManagerToken) {
        HttpResponse<JsonNode> accessTokenResonse = jsonNodeResponse(Unirest.get(baseUrl +"/api/v2/users/" + userId)
                .header("Authorization", "Bearer " + auth0ManagerToken));
        log.debug("Google response {}", accessTokenResonse.getBody());
        JSONObject identities = accessTokenResonse.getBody().getObject()
                .getJSONArray("identities").getJSONObject(0);
        return new Tuple2<>(identities.optString("access_token"), identities.optString("refresh_token"));
    }

    /**
     * Abstraction over a simple request with exception handling
     * @param request request to execute
     * @return result of the request if successful
     */
    private HttpResponse<JsonNode> jsonNodeResponse(BaseRequest request) {
        HttpResponse<JsonNode> response = Try.of(request::asJson)
                .getOrElseThrow(() -> new RuntimeException("Some problem with executing "+request.getHttpRequest().getUrl()+" request"));
        log.debug("Receive {} response from {}", response.getStatusText(), request.getHttpRequest().getUrl());
        return response;
    }

    private Tuple2<String, String> loadTokens(String userToken) {
        log.info("Loading auth0 tokens");
        String userId = getUserId(userToken);
        String auth0ManagerToken = getAuth0ManagerToken();

        Tuple2<String, String> tokens = getGoogleTokens(userId, auth0ManagerToken);
        log.debug("userId:"+userId+" access_token:"+ tokens._1 +", refresh_token:"+ tokens._2);
        return tokens;
    }

    @Cacheable(value = "googleTokensCache")
    public Tuple2<String, String> getTokens(String userToken) {
        return loadTokens(userToken);
    }

    private JSONObject preparedBody() {
        return new JSONObject()
                .put("grant_type", "client_credentials")
                .put("client_id", clientId)
                .put("client_secret", clientSecret)
                .put("audience", audience);
    }

}
