package com.shmigel.scheduleManager.dialogflow.model.response;

import lombok.Data;

@Data
public class Suggestion {

    private String title;

    public Suggestion(String title) {
        this.title = title;
    }

}
