package com.shmigel.scheduleManager.dialogflow.model;

import lombok.Data;

@Data
public class TextResponse extends Response {

    private String fulfillmentText;

    public TextResponse(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    public TextResponse() {
    }

    public static TextResponse getUnknownAnswer() {
        return new TextResponse("I don't understand you. Please try again");
    }

}
