package com.shmigel.scheduleManager.dialogflow.model;

import com.shmigel.scheduleManager.dialogflow.model.response.RichResponseBuilder;
import lombok.Data;

@Data
public class GoogleResponse extends Response {

    private RichResponseBuilder google;

    public GoogleResponse(RichResponseBuilder google) {
        this.google = google;
    }
}
