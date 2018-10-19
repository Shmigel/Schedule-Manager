package com.shmigel.scheduleManager.service;

import com.shmigel.scheduleManager.model.SpeechBreakStrength;

import java.util.function.Supplier;

public class Speech {

    private StringBuilder message = new StringBuilder();

    public Speech() {
        message.append("<speak> ");
    }

    public Speech say(String text) {
        message.append(text +" ");
        return this;
    }

    public Speech sayIf(Supplier<Boolean> condition, String text) {
        if (condition.get()) {
            say(text);
        }
        return this;
    }

    public Speech sayIf(String text, Supplier<Object> exception) {
        Object o = exception.get();
        if (o != null) {
            say(text.replace("_", o.toString()));
        }
        return this;
    }

    public Speech pause(String time) {
        message.append("<break time=\""+time+"\"/>");
        return this;
    }

    public Speech pause(String time, SpeechBreakStrength breakStrength) {
        message.append("<break time=\""+time+"\" strength=\""+breakStrength.value()+"\"/>");
        return this;
    }

    public Speech sayAs(String interpretAs, String text) {
        message.append("<say-as interpret-as=\""+interpretAs+"\">"+text+"</say-as> ");
        return this;
    }

    public Speech sayAsDate(String format, String date) {
        message.append("<say-as interpret-as=\"date\" format=\""+format+"\">"+date+"</say-as> ");
        return this;
    }

    public Speech sayAsDate(String format, String details, String date) {
        message.append("<say-as interpret-as=\"date\" format=\""+format+"\" detail=\""+details+"\">"+date+"</say-as> ");
        return this;
    }

    public Speech sayAsTime(String format, String time) {
        message.append("<say-as interpret-as=\"time\" format=\""+format+"\">"+time+"</say-as> ");
        return this;
    }

    public Speech point() {
        message.append(". ");
        return this;
    }

    public String build() {
        return message.append("</speak>").toString();
    }
}
