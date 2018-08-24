package com.shmigel.scheduleManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shmigel.scheduleManager.BeansConfiguration;
import com.shmigel.scheduleManager.model.dialogflow.Request;
import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ReadListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class DialogflowInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Scanner s = new Scanner(request.getInputStream()).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        System.out.println(result);
        return true;
    }
}
