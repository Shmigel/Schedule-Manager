package com.shmigel.scheduleManager.model.dialogflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OriginalDetectIntentRequest {

    private Payload payload;

    public OriginalDetectIntentRequest() {
    }
}
