package com.shmigel.scheduleManager.dialogflow.model.response;

import lombok.Data;

@Data
public class Payload {

    private Platform google;

    public Payload(Platform google) {
        this.google = google;
    }
}
