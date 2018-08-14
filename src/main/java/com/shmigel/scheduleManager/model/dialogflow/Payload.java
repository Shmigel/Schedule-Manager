package com.shmigel.scheduleManager.model.dialogflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Payload {

    private User user;

    public Payload() {
    }
}
