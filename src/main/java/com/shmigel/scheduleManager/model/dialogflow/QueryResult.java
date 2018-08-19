package com.shmigel.scheduleManager.model.dialogflow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResult {

    private String queryText;

    private String action;

    private Map<String, String> parameters = new HashMap<>();

    public QueryResult() {
    }

    @JsonCreator
    public QueryResult(
            @JsonProperty("queryText") String queryText,
            @JsonProperty("action") String action,
            @JsonProperty("parameters") Map<String, String> parameters) {
        this.queryText = queryText;
        this.action = action;
        this.parameters = parameters;
    }
}
