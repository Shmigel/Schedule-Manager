package com.shmigel.scheduleManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.config.GoogleBeanConfiguration;
import com.shmigel.scheduleManager.dialogflow.controller.DialogflowEventControllerInvoker;
import com.shmigel.scheduleManager.dialogflow.model.GoogleResponse;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import com.shmigel.scheduleManager.dialogflow.model.request.Request;
import com.shmigel.scheduleManager.dialogflow.model.request.User;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponse;
import com.shmigel.scheduleManager.dialogflow.model.response.RichResponseBuilder;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    private final DialogflowEventControllerInvoker controllerInvoker;

    private final GoogleBeanConfiguration configuration;

    @Autowired
    public BaseController(DialogflowEventControllerInvoker controllerInvoker, GoogleBeanConfiguration configuration) {
        this.controllerInvoker = controllerInvoker;
        this.configuration = configuration;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> testGetMethod(@RequestParam(required = false) Map<String, String> parameters) {
        return ResponseEntity.ok("It's working \n"+ parameters);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<?> baseController(@RequestBody Request request, @RequestHeader HttpHeaders headers) {
        logger.debug("Post request with headers: {}, and body: {}", headers, request);
        setupAuth0Token(request);
        if (request.getQueryResult().getAction().equals("DAY_EVENTS")) {
            return ResponseEntity.ok().body(response);
        }
        Response response = controllerInvoker.invokeProperMethod(request);
        ResponseEntity<Response> okResponse = ResponseEntity.ok().body(response);
        logger.debug("TextResponse: {}", okResponse.getBody());
        return okResponse;
    }
    
    private void setupAuth0Token(Request request) {
        User user = request.getOriginalDetectIntentRequest().getPayload().getUser();
        configuration.setAuth0Token(user.getUserId(), user.getAccessToken());
    }
    
    String response = "";
    @RequestMapping(value = "/set" , method = RequestMethod.POST)
    public ResponseEntity<String> temporary(@RequestBody String body) {
        response = body;
        return ResponseEntity.ok("Set successful");
    }

    private Request getRequest(String body) {
        return Try.of(() -> new ObjectMapper().readValue(body, Request.class))
                .getOrElseThrow(() -> new RuntimeException());
    }



}
