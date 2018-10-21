package com.shmigel.scheduleManager.dialogflow.model;

import lombok.Data;

@Data
public class TextResponse extends Response {

    public TextResponse(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }

    public static TextResponse getUnknownAnswer() {
        return new TextResponse("I don't understand you. Please try again");
    }

    public TextResponse() {
    }

    private String fulfillmentText;

}
