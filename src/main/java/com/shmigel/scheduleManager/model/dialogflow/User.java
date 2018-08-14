package com.shmigel.scheduleManager.model.dialogflow;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private String accessToken;

    private String locate;

    public User() {
    }
}
