package com.shmigel.scheduleManager.model.dialogflow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data

@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

    private OriginalDetectIntentRequest originalDetectIntentRequest;

    @JsonCreator
    public Request(
            @JsonProperty("originalDetectIntentRequest") OriginalDetectIntentRequest originalDetectIntentRequest) {
        this.originalDetectIntentRequest = originalDetectIntentRequest;
    }

    public Request() {
    }
}
