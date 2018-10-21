package com.shmigel.scheduleManager.dialogflow.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {

    private User user;

    @JsonCreator
    public Payload(
            @JsonProperty("user") User user) {
        this.user = user;
    }

    public Payload() {
    }
}
