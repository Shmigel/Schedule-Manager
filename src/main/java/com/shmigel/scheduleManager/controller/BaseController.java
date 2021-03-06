package com.shmigel.scheduleManager.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.shmigel.scheduleManager.config.GoogleBeanConfiguration;
import com.shmigel.scheduleManager.dialogflow.controller.DialogflowEventControllerInvoker;
import com.shmigel.scheduleManager.dialogflow.model.request.Request;
import com.shmigel.scheduleManager.dialogflow.model.request.User;
import com.shmigel.scheduleManager.dialogflow.model.response.DialogflowResponse;
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
    public ResponseEntity<DialogflowResponse> baseController(@RequestBody Request request, @RequestHeader HttpHeaders headers) {
        logger.debug("Post request body: {}", request);
        setupAuth0Token(request);
        DialogflowResponse response = controllerInvoker.invokeProperMethod(request);
        ResponseEntity<DialogflowResponse> okResponse = ResponseEntity.ok().body(response);
        logger.debug("Response: {}", okResponse.getBody());
        return okResponse;
    }

    /**
     * Set authToken in {@link GoogleBeanConfiguration} class in order to later load google tokens
     * and create {@link GoogleCredential} bean.
     *
     * @see GoogleBeanConfiguration#googleCredential(String)
     * @param request get authToken called accessToken in request JSON
     */
    private void setupAuth0Token(Request request) {
        User user = request.getOriginalDetectIntentRequest().getPayload().getUser();
        configuration.setAuth0Token(user.getAccessToken());
    }
}
