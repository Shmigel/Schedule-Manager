package com.shmigel.scheduleManager.dialogflow.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String lastSeen;

    private String accessToken;

    private String locale;

    private String userId;

    @JsonCreator
    public User(
            @JsonProperty("lastSeen") String lastSeen,
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("locale") String locale,
            @JsonProperty("userId") String userId) {
        this.lastSeen = lastSeen;
        this.accessToken = accessToken;
        this.locale = locale;
        this.userId = userId;
    }

    public User() {
    }
}
