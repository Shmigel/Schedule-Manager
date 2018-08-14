package com.shmigel.scheduleManager.model.dialogflow;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
