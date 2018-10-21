package com.shmigel.scheduleManager.dialogflow.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

    private QueryResult queryResult;

    private OriginalDetectIntentRequest originalDetectIntentRequest;

    @JsonCreator
    public Request(
            @JsonProperty("queryResult") QueryResult queryResult,
            @JsonProperty("originalDetectIntentRequest") OriginalDetectIntentRequest originalDetectIntentRequest) {
        this.queryResult = queryResult;
        this.originalDetectIntentRequest = originalDetectIntentRequest;
    }

    public Request() {
    }
}
