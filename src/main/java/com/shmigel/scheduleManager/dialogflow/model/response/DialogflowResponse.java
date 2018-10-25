package com.shmigel.scheduleManager.dialogflow.model.response;

import lombok.Data;

@Data
public class DialogflowResponse {

    private String fulfillmentText;

    private Payload payload;

    public DialogflowResponse(String fulfillmentText, Payload payload) {
        this.fulfillmentText = fulfillmentText;
        this.payload = payload;
    }

    public DialogflowResponse() {
        this("Something was going wrong. Please try later", null);
    }

}
