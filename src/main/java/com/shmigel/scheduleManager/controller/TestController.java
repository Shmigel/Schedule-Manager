package com.shmigel.scheduleManager.controller;

import com.shmigel.scheduleManager.model.EventController;
import com.shmigel.scheduleManager.model.EventMapping;
import com.shmigel.scheduleManager.model.dialogflow.Response;

@EventController
public class TestController {

    @EventMapping("TEST_EVENT")
    public Response testEvent() {
        return new Response("Test is successful");
    }

    @EventMapping("ADD_EVENT")
    public Response addEvent() {
        return new Response("Event's scheduled");
    }

    @EventMapping("CREATE_SCHEDULE")
    public Response createSchedule() {
        return new Response("Schedule's created");
    }

    @EventMapping("ADD_USER")
    public Response addUser() {
        return new Response("User's added");
    }
}
