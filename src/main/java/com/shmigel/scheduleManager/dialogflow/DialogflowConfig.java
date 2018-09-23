package com.shmigel.scheduleManager.dialogflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class DialogflowConfig {

    @Bean
    DialogflowEventControllerListener dialogflowEventControllerListener() {
        return new DialogflowEventControllerListener();
    }

    @Bean
    Set<MethodWrapper> methods(DialogflowEventControllerListener dialogflowEventControllerListener) {
        return dialogflowEventControllerListener.getMethodWrappers();
    }

    @Bean
    DialogflowEventControllerInvoker dialogflowEventControllerInvoker() {
        return new DialogflowEventControllerInvoker();
    }

}
