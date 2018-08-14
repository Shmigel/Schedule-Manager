package com.shmigel.scheduleManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.model.dialogflow.Request;
import com.shmigel.scheduleManager.model.dialogflow.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BaseController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> testGetMethod(@RequestBody String text) {
        return ResponseEntity.ok("It's working "+text);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Response> baseController(@RequestBody Request request) {
        return ResponseEntity.ok(Response.builder().fulfilmentText("Successful").build());
    }

}
