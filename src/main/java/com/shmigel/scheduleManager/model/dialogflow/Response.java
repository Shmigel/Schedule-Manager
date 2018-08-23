package com.shmigel.scheduleManager.model.dialogflow;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
