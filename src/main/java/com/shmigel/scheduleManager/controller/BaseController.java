package com.shmigel.scheduleManager.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.shmigel.scheduleManager.BeansConfiguration;
import com.shmigel.scheduleManager.GoogleCalendar;
import com.shmigel.scheduleManager.config.DialogflowEventControllerInvoker;
import com.shmigel.scheduleManager.model.dialogflow.Request;
import com.shmigel.scheduleManager.model.dialogflow.Response;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private DialogflowEventControllerInvoker controllerInvoker;

    @Autowired
    private BeansConfiguration beansConfiguration;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> testGetMethod() {
        return ResponseEntity.ok("It's working");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Response> baseController(@RequestBody Request request) {
        beansConfiguration.setAuthToken(request.getOriginalDetectIntentRequest().getPayload().getUser().getAccessToken());
        logger.debug("Post request with body: {}", request);
        Response response = controllerInvoker.invokeProperMethod(request);
        logger.debug("Response: {}", response);
        return ResponseEntity.ok(response);
    }



}
