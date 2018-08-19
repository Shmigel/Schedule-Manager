package com.shmigel.scheduleManager.model.dialogflow;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {

    public Response(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    public Response() {
    }

    private String fulfillmentText;

}
