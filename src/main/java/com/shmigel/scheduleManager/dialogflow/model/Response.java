package com.shmigel.scheduleManager.dialogflow.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response {

    public Response(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    public static Response getUnknownAnswer() {
        return new Response("I don't understand you. Please try again");
    }

    public Response() {
    }

    private String fulfillmentText;

}
