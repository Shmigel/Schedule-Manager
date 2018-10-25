package com.shmigel.scheduleManager.dialogflow.model.response.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeName("simpleResponse")
@Data
@EqualsAndHashCode(callSuper = false)
public class SimpleResponse extends Message {

    private String textToSpeech;

    private String displayText;

    public SimpleResponse(String textToSpeech, String displayText) {
        this.textToSpeech = textToSpeech;
        this.displayText = displayText;
    }
}
