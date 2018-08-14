package com.shmigel.scheduleManager.model.dialogflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Request {

    private OriginalDetectIntentRequest originalDetectIntentRequest;

    public Request() {
    }
}
