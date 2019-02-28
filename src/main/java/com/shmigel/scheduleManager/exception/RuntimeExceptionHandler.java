package com.shmigel.scheduleManager.exception;

import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.message.SimpleResponseBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RuntimeExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${project.debugEnable}")
    private boolean debugEnable;

    @ExceptionHandler(value = ResponseProneException.class)
    public ResponseEntity<DialogflowResponse> responseResponseEntity(RuntimeException ex) {
        DialogflowResponse response =
                new RichResponse().of(new SimpleResponseBuilder().say(ex.getMessage()).build());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<DialogflowResponse> defaultOnFailResponse(RuntimeException ex) {
        DialogflowResponse response =
                new RichResponse().of(new SimpleResponseBuilder()
                        .say(debugEnable? ex.getMessage() : "There is some problem on our side")
                        .build());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
    }

}
