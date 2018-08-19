package com.shmigel.scheduleManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.config.DialogflowEventControllerInvoker;
import com.shmigel.scheduleManager.model.dialogflow.Request;
import com.shmigel.scheduleManager.model.dialogflow.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> testGetMethod(@RequestBody String text) {
        return ResponseEntity.ok("It's working "+text);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Response> baseController(@RequestBody Request request) {
        logger.debug("Post request with body: {}", request);

        //        ResponseEntity<Response> response = ResponseEntity.ok(Response.builder().fulfillmentText("Successful").build());
        Response response = controllerInvoker.invokeProperMethod(request);


        logger.debug("Response: {}", response);
        return ResponseEntity.ok(response);
    }

}
