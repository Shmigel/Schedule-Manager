package com.shmigel.scheduleManager.dialogflow.model.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonTypeName("simpleResponse")
public class SimpleResponse extends ResponseElement {

    private String textToSpeech;

    private String displayText;

    public SimpleResponse(String textToSpeech, String displayText) {
        this.textToSpeech = textToSpeech;
        this.displayText = displayText;
    }
}
