package com.shmigel.scheduleManager.dialogflow.model.response;

import lombok.Data;

@Data
public class RichResponseBuilder {

    private RichResponse richResponse;

    public RichResponseBuilder(RichResponse richResponse) {
        this.richResponse = richResponse;
    }
}
