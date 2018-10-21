package com.shmigel.scheduleManager.dialogflow.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OriginalDetectIntentRequest {

    private Payload payload;

    @JsonCreator
    public OriginalDetectIntentRequest(
            @JsonProperty("payload") Payload payload) {
        this.payload = payload;
    }

    public OriginalDetectIntentRequest() {
    }
}
