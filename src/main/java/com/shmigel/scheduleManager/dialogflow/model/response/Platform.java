package com.shmigel.scheduleManager.dialogflow.model.response;

import lombok.Data;

@Data
public class Platform {

    private RichResponse richResponse;

    public Platform(RichResponse richResponse) {
        this.richResponse = richResponse;
    }
}
