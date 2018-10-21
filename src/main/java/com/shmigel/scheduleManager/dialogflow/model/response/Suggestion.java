package com.shmigel.scheduleManager.dialogflow.model.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
public class Suggestion {

    private String title;

    public Suggestion(String title) {
        this.title = title;
    }
}
