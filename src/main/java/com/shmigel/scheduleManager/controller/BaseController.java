package com.shmigel.scheduleManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.config.GoogleBeanConfiguration;
import com.shmigel.scheduleManager.dialogflow.DialogflowEventControllerInvoker;
import com.shmigel.scheduleManager.dialogflow.model.Request;
import com.shmigel.scheduleManager.dialogflow.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private DialogflowEventControllerInvoker controllerInvoker;

    @Autowired
    private GoogleBeanConfiguration configuration;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> testGetMethod(@RequestParam(required = false) Map<String, String> parameters) {
        return ResponseEntity.ok("It's working \n"+ parameters);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Response> baseController(@RequestBody String body, @RequestHeader HttpHeaders headers) {
        logger.debug("Post request with headers: {}, and body: {}", headers, body);
        Request request = getRequest(body);
        Optional<String> accessToken = Optional.ofNullable(request.getOriginalDetectIntentRequest().getPayload().getUser().getAccessToken());
        configuration.setAuth0Token(accessToken.orElseThrow(RuntimeException::new));
        Response response = controllerInvoker.invokeProperMethod(request);
        logger.debug("Response: {}", response);
        return ResponseEntity.ok(response);
    }

    private Request getRequest(String body) {
        Request r = null;
         try {
             r = new ObjectMapper().readValue(body, Request.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return r;
    }



}
