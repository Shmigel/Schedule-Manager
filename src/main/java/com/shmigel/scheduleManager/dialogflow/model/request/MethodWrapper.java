package com.shmigel.scheduleManager.dialogflow.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodWrapper {
    private Method method;
    private String action;
}
