package com.shmigel.scheduleManager.dialogflow.model.response.message;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.shmigel.scheduleManager.model.SpeechBreakStrength;
import lombok.Data;

import java.util.function.Supplier;

@Data
@JsonTypeName("simpleResponse")
public class SimpleResponseBuilder extends Message {

    private StringBuilder textToSpeech = new StringBuilder();

    private StringBuilder displayText = new StringBuilder();

    public SimpleResponseBuilder() {
        textToSpeech.append("<speak> ");
    }

    public SimpleResponseBuilder say(String text) {
        textToSpeech.append(text +" ");
        displayText.append(text+" ");
        return this;
    }

    public SimpleResponseBuilder sayIf(Supplier<Boolean> condition, String text) {
        if (condition.get()) {
            say(text);
        }
        return this;
    }

    public SimpleResponseBuilder sayIf(String text, Supplier<Object> exception) {
        Object o = exception.get();
        if (o != null) {
            say(text.replace("_", o.toString()));
        }
        return this;
    }

    public SimpleResponseBuilder sayIfElse(String text, Supplier<Object> exception, String elseText) {
        Object o = exception.get();
        if (o != null) {
            say(text.replace("_", o.toString()));
        } else {
            say(elseText);
        }
        return this;
    }

    public SimpleResponseBuilder pause(String time) {
        textToSpeech.append("<break time=\""+time+"\"/>");
        return this;
    }

    public SimpleResponseBuilder pause(String time, SpeechBreakStrength breakStrength) {
        textToSpeech.append("<break time=\""+time+"\" strength=\""+breakStrength.value()+"\"/>");
        return this;
    }

    public SimpleResponseBuilder sayAs(String interpretAs, String text) {
        textToSpeech.append("<say-as interpret-as=\""+interpretAs+"\">"+text+"</say-as> ");
        displayText.append(text+" ");
        return this;
    }

    public SimpleResponseBuilder sayAsDate(String format, String date) {
        textToSpeech.append("<say-as interpret-as=\"date\" format=\""+format+"\">"+date+"</say-as> ");
        displayText.append(date+" ");
        return this;
    }

    public SimpleResponseBuilder sayAsDate(String format, String details, String date) {
        textToSpeech.append("<say-as interpret-as=\"date\" format=\""+format+"\" detail=\""+details+"\">"+date+"</say-as> ");
        displayText.append(date+" ");
        return this;
    }

    public SimpleResponseBuilder sayAsTime(String format, String time) {
        textToSpeech.append("<say-as interpret-as=\"time\" format=\""+format+"\">"+time+"</say-as> ");
        displayText.append(time+" ");
        return this;
    }

    public SimpleResponseBuilder newLine() {
        displayText.append("\n");
        return this;
    }

    public SimpleResponseBuilder point() {
        displayText.append(". ");
        return this;
    }

    public SimpleResponse build() {
        return new SimpleResponse(textToSpeech.append("</speak>").toString(), displayText.toString());
    }

}
